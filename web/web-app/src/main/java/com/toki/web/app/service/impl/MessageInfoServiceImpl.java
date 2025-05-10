package com.toki.web.app.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.common.exception.LeaseException;
import com.toki.model.entity.MessageInfo;
import com.toki.model.entity.UserInfo;
import com.toki.web.app.mapper.MessageInfoMapper;
import com.toki.web.app.service.MessageInfoService;
import com.toki.web.app.service.UserInfoService;
import com.toki.web.app.vo.message.ChatSessionVo;
import com.toki.web.app.vo.user.UserInfoVo;
import com.toki.web.app.websocket.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import static com.toki.common.constant.RedisConstant.CHAT_KEY;
import static com.toki.common.result.ResultCodeEnum.*;
import static com.toki.model.enums.BaseStatus.DISABLE;
import static com.toki.model.enums.MessageStatus.READ;
import static com.toki.model.enums.MessageStatus.UNREAD;

/**
 * @author toki
 * @since 2025-04-25
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoMapper, MessageInfo> implements MessageInfoService {

    private final MessageInfoMapper messageInfoMapper;

//    private final GroupFollowService followService;
    private final UserInfoService userService;
    private final RedisService redisService;

    private final StringRedisTemplate stringRedisTemplate;
    // 限制聊天记录数量
    final Integer limitMsgLength = 6000;
    // 限制用户数量
//    final Integer limitUserLength = 300;

    /**
     * 统计用户的聊天会话列表中未读消息的数量
     * 聊天会话列表组成：
     * 1. 关注的用户的会话
     * 2. 发过消息的用户的会话
     *
     * @param userId 用户ID
     */
    @Override
    public Long unReadMsgCnt(Long userId) {
        final ArrayList<Long> sessionUserIds = getSessionUserIds(userId);
        // 没有关注or发过消息的任何用户，返回0
        if (sessionUserIds.isEmpty()) {
            return 0L;
        }
        return getUnReadCount(userId, sessionUserIds);
    }

    /**
     * 有新消息时，保存消息到数据库
     */
    @Override
    public void sendMsg(MessageInfo message) {
        // 数据校验
        final Long sendUserId = message.getSendUserId();
        final Long receiveUserId = message.getReceiveUserId();
        userStatusCheck(message.getSendUserId(), receiveUserId);
        if (StrUtil.isBlank(message.getContent())) {
            throw new LeaseException(MESSAGE_CONTENT_EMPTY_ERROR);
        }
        // 限制消息长度
        if (message.getContent().length() > limitMsgLength) {
            throw new LeaseException(MESSAGE_CONTENT_TOO_LONG_ERROR);
        }
        // 判断是否已读(针对此消息的接收者)
        // 判断接收者是否正在查看发送者的聊天,即接收者是否有当前会话的ID
        boolean receiverIsViewing = isCurrentChatSession(receiveUserId, sendUserId);
        // 如果是当前会话的ID，同时标记为已读
        if (receiverIsViewing) {
            message.setIsRead(READ);
        } else {
            message.setIsRead(UNREAD);
        }

        save(message);
    }

    /**
     * 进入聊天页面，查找两人之间的聊天记录(此处是当前用户与指定用户)，标记已读，并返回
     *
     * @param currentUserId 当前用户
     * @param receiveUserId 指定用户
     * @return List<MessageInfo> 聊天记录列表
     */
    @Override
    public List<MessageInfo> chatHistory(Long currentUserId, Long receiveUserId) {
        // 数据校验
        userStatusCheck(currentUserId, receiveUserId);
        // 查找两人之间的聊天记录,按留言时间排序,并限制消息数量,id传参顺序任意
        final List<MessageInfo> messageInfos = messageInfoMapper.selectChatHistory(currentUserId, receiveUserId, limitMsgLength);
        if (messageInfos.isEmpty()) {
            return new ArrayList<>();
        }

        final List<String> messageIds = messageInfos.stream().map(MessageInfo::getId).toList();

        // 将对方发送的消息在数据库中更新为已读
//        messageInfoMapper.updateIsRead(currentUserId, receiveUserId, messageIds);
        // 分批更新，避免单条 SQL 语句过大，同时减少数据库的一次性负载。
        int batchSize = 1000;
        for (int i = 0; i < messageIds.size(); i += batchSize) {
            int end = Math.min(i + batchSize, messageIds.size());
            List<String> batchIds = messageIds.subList(i, end);
            // 将接收方为当前用户的消息更新为已读
            messageInfoMapper.updateIsRead(receiveUserId, currentUserId, batchIds);
        }

        // 返回聊天记录，isRead属性与数据库不同步不影响显示
        return messageInfos;
        // 1个or连接两个and条件组
//        return this.query()
//                .and(wrapper ->
//                        wrapper.eq("send_user_id", currentUserId)
//                                .eq("receive_user_id", receiveUserId))
//                .or()
//                .and(wrapper ->
//                        wrapper.eq("send_user_id", receiveUserId)
//                                .eq("receive_user_id", currentUserId))
//                .orderBy(true, true, "create_time")
//                .last("limit " + limitMsgLength)
//                .list();
    }

    /**
     * 获取用户的聊天会话列表
     *
     * @param userId 用户ID
     * @return 聊天会话列表
     */
    @Override
    public List<ChatSessionVo> getSessions(Long userId) {
        // 获取聊天会话列表相关的用户ID列表
        final ArrayList<Long> sessionUserIds = getSessionUserIds(userId);
        if (sessionUserIds.isEmpty()) {
            return new ArrayList<>();
        }

        final ArrayList<ChatSessionVo> sessionVoList = new ArrayList<>();
        // 一个个查出来，然后封装
        for (Long sessionUserId : sessionUserIds) {
            final ChatSessionVo sessionVo = new ChatSessionVo();
            // 最新的聊天信息对象
            final MessageInfo lastMessage = this.query()
                    .eq("send_user_id", sessionUserId).or().eq("receive_user_id", sessionUserId)
                    .orderBy(true, false, "create_time")
                    .last("limit 1").one();
            if (lastMessage != null) {
                // 最新一条聊天记录的时间,最新一条聊天记录
                sessionVo.setLastMessageTime(lastMessage.getCreateTime())
                        .setLastMessage(lastMessage.getContent());
            }
            // 单个用户信息
            final UserInfo userInfo = userService.getById(sessionUserId);
            sessionVo.setUserInfo(CglibUtil.copy(userInfo, UserInfoVo.class));
            // 单个用户的未读消息数量
            final long unReadCount = getUnReadCount(userId, new ArrayList<>(Collections.singleton(sessionUserId)));
            sessionVo.setUnreadCount((int) unReadCount);

            // 单个用户在线标识
            boolean isOnline = redisService.isUserOnline(sessionUserId);
            sessionVo.setIsOnline(isOnline);

            sessionVoList.add(sessionVo);
        }

        return sessionVoList;
    }

    // 判断接收者是否正在查看发送者的聊天,即接收者是否有当前会话的ID
    @Override
    public boolean isCurrentChatSession(Long currentUserId, Long sendUserId) {
        final String currentChatSessionId = stringRedisTemplate.opsForValue().get(CHAT_KEY + currentUserId);
        if (StrUtil.isBlank(currentChatSessionId)) {
            return false;
        }
        return Long.valueOf(currentChatSessionId).equals(sendUserId);
    }

    // 统计userId的消息列表sessionUserIds的未读消息总数量
    @Override
    public long getUnReadCount(Long userId, List<Long> sessionUserIds) {
        // 查询关注列表中每个用户的未读消息数量，并计算总和
        long unReadCount = 0L;
        for (Long sessionUserId : sessionUserIds) {
            // 接收者为当前用户，发送者为关注用户或发过消息的用户，未读，非机器人
            LambdaQueryWrapper<MessageInfo> queryWrapper = new LambdaQueryWrapper<MessageInfo>()
                    .eq(MessageInfo::getReceiveUserId, userId)
                    .eq(MessageInfo::getSendUserId, sessionUserId)
                    .eq(MessageInfo::getIsRead, 0)
                    .eq(MessageInfo::getIsAi, 0);
            unReadCount += count(queryWrapper);
        }
        return unReadCount;
    }

    // 获取聊天会话列表相关的用户ID列表，然后排序
    @NotNull
    private ArrayList<Long> getSessionUserIds(Long userId) {
        // 查询出聊天会话的用户ID列表，并去重
        List<Long> sessionUserIds = messageInfoMapper.getSessionUserIds(userId);

        sessionUserIds.remove(userId);
        if (sessionUserIds.isEmpty()) {
            return new ArrayList<>();
        }


        // 排序 发过消息的
//        List<Long> sortedMessages = messageInfoMapper.querySortedMessage(sessionUserIds);

        // 排序
        //todo待优化：每个会话只取最新的一条消息，并按时间倒序排列
        // 根据用户ID查询消息记录，并按照创建时间倒序排列
        final List<MessageInfo> sortedMessages = this.query()
//                .in("send_user_id", sessionUserIds)
//                .or()
//                .in("receive_user_id", sessionUserIds)
                // nested()：嵌套查询， 创建一个独立的查询条件块，不带默认的 AND 或 OR 逻辑,确保每个条件块是独立的。
                .nested(w->w.in("send_user_id", sessionUserIds).eq("receive_user_id", userId))
                .or()
                .nested(w->w.in("receive_user_id", sessionUserIds).eq("send_user_id", userId))
                .orderByDesc("create_time")
                .list();


        final LinkedHashSet<Long> sortedUserIds = new LinkedHashSet<>();
        if (!sortedMessages.isEmpty()) {
            // 提取消息记录中的用户ID，并去重，由于已经按照时间排序，可以使用LinkedHashSet保持顺序
            for (MessageInfo messageInfo : sortedMessages) {
                if (messageInfo.getSendUserId().equals(userId)) {
                    sortedUserIds.add(messageInfo.getReceiveUserId());
                } else if (messageInfo.getReceiveUserId().equals(userId)) {
                    sortedUserIds.add(messageInfo.getSendUserId());
                }
            }
        }
        // 关注但未发过消息的排在最后面
        sortedUserIds.addAll(sessionUserIds);
        sortedUserIds.remove(userId);

        log.warn("当前用户ID: {}", userId);
        log.warn("会话列表ID，去重后: {}", sessionUserIds);
        log.warn("排序后：sortedUserIds: {}", sortedUserIds);
        return new ArrayList<>(sortedUserIds);
    }

    // 判断用户状态
    private void userStatusCheck(Long sendUserId, Long receiveUserId) {
        final UserInfo receiveUser = userService.getById(receiveUserId);
        final UserInfo sendUser = userService.getById(sendUserId);
        if (receiveUser == null) {
            throw new LeaseException(USER_NOT_EXIST_ERROR);
        }
        if (sendUser == null) {
            throw new LeaseException(USER_NOT_EXIST_ERROR);
        }
        if (sendUser.getStatus() == DISABLE) {
            throw new LeaseException(ADMIN_ACCOUNT_DISABLED_ERROR);
        }
        if (receiveUser.getStatus() == DISABLE) {
            throw new LeaseException(ADMIN_ACCOUNT_DISABLED_ERROR);
        }
    }
}

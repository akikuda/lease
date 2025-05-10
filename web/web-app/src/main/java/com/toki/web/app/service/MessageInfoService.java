package com.toki.web.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.model.entity.MessageInfo;
import com.toki.web.app.vo.message.ChatSessionVo;

import java.util.List;

/**
 * @author toki
 * @since 2025-04-25
 */
public interface MessageInfoService extends IService<MessageInfo> {

    /**
     * 根据用户ID获取总的未读消息数量
     * */
    Long unReadMsgCnt(Long userId);

    void sendMsg(MessageInfo message);

    /**
     * 获取两人之间的聊天记录
     * */
    List<MessageInfo> chatHistory(Long sendUserId, Long receiveUserId);

    /**
     * 根据用户ID获取所有会话列表
     * */
    List<ChatSessionVo> getSessions(Long userId);

    // 判断接收者是否正在查看发送者的聊天,即接收者是否有当前会话的ID
    boolean isCurrentChatSession(Long currentUserId, Long sendUserId);

    // 统计userId的消息列表sessionUserIds的未读消息总数量
    long getUnReadCount(Long userId, List<Long> sessionUserIds);
}


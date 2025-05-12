package com.toki.web.app.controller.message;


import com.toki.common.result.Result;
import com.toki.common.utils.LoginUserHolder;
import com.toki.model.entity.MessageInfo;
import com.toki.web.app.service.MessageInfoService;
import com.toki.web.app.vo.message.ChatSessionVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.toki.common.constant.RabbitMqConstant.MESSAGE_FANOUT;
import static com.toki.common.constant.RabbitMqConstant.MESSAGE_KEY;
import static com.toki.common.constant.RedisConstant.CHAT_KEY;

/**
 * <p>
 * 会话信息表 前端控制器
 * </p>
 *
 * @author toki
 * @since 2025-04-25
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/app/message")
@Slf4j
public class MessageInfoController {

    private final MessageInfoService msgService;
    private final StringRedisTemplate stringRedisTemplate;
//    private final RabbitTemplate rabbitTemplate;

    @Operation(summary = "获取总的未读消息数量")
    @GetMapping("/unread")
    public Result<Long> unReadMsgCnt() {
        // 获取当前用户ID
        final Long userId = LoginUserHolder.getLoginUser().getUserId();
        return Result.ok(msgService.unReadMsgCnt(userId));
    }

    // 发送消息时保存消息到数据库
//    @Operation(summary = "保存消息")
//    @PostMapping("/send")
//    public Result sendMsg(@RequestBody MessageInfo message) {
////        msgService.sendMsg(message);
//        log.warn("前端请求发送消息sendMsg: {}", message);
////        message.setId(UUID.fastUUID().toString(true));
//        rabbitTemplate.convertAndSend(MESSAGE_FANOUT, MESSAGE_KEY, message);
//        return Result.ok();
//    }

    /**
     * 更新当前会话ID
     * @param userId 当前用户ID
     * @param currentChatId 当前会话ID, -1表示退出会话
     * */
    @Operation(summary = "更新当前会话ID")
    @PostMapping("/updateCurrentChatId")
    public Result updateCurrentChatId(
            @RequestParam("userId") Long userId,
            @RequestParam("currentChatId") Long currentChatId) {
        // 使用Redis保存当前会话ID
        if (currentChatId != -1L) {
            // 表示有新的会话ID
            stringRedisTemplate.opsForValue().set(CHAT_KEY + userId, currentChatId.toString());
        } else {
            // 表示退出会话，删除Redis中保存的会话ID
            stringRedisTemplate.delete(CHAT_KEY + userId);
        }
        return Result.ok();
    }

    @Operation(summary = "获取两人之间的聊天记录")
    @GetMapping("/chat/history")
    public Result<List<MessageInfo>> chatHistory(
            @RequestParam("sendUserId") Long sendUserId,
            @RequestParam("receiveUserId") Long receiveUserId) {
        return Result.ok(msgService.chatHistory(sendUserId, receiveUserId));
    }

    @Operation(summary = "获取所有聊天会话列表")
    @GetMapping("/sessions")
    public Result<List<ChatSessionVo>> getSessions() {
        // 获取当前用户ID
        final Long userId = LoginUserHolder.getLoginUser().getUserId();
        return Result.ok(msgService.getSessions(userId));
    }

}

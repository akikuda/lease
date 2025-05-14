package com.toki.web.app.mq;

import com.toki.web.app.service.AiChatHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.toki.common.constant.RabbitMqConstant.AI_MESSAGE_QUEUE;

/**
 * 聊天历史记录消费者服务
 *
 * @author toki
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatHistoryConsumer {

    private final AiChatHistoryService aiChatHistoryService;

    /**
     * 处理保存聊天历史记录ID的消息
     */
    @RabbitListener(queues = AI_MESSAGE_QUEUE)
    public void handleSaveChatHistory(Map<String, Object> messageMap) {
        try {
            String type = (String) messageMap.get("type");
            String sessionId = (String) messageMap.get("sessionId");
            Integer userIdInt = (Integer) messageMap.get("userId");
            long userId = userIdInt.longValue();
//            String content = (String) messageMap.get("content");

            log.warn("接收到AI聊天历史记录保存消息: type={}, sessionId={}, userId={}", type, sessionId, userId);

            // 保存会话ID记录
            aiChatHistoryService.save(type, sessionId, userId);
            // 保存会话内容记录
//            aiChatHistoryService.save(type, sessionId, userId, content);

            log.warn("AI聊天历史记录 异步保存成功");
        } catch (Exception e) {
            log.error("AI聊天历史记录 异步保存失败", e);
        }
    }
} 
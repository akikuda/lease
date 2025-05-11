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
     * 处理保存聊天历史记录的消息
     */
    @RabbitListener(queues = AI_MESSAGE_QUEUE)
    public void handleSaveChatHistory(Map<String, Object> messageMap) {
        try {
            String type = (String) messageMap.get("type");
            String sessionKey = (String) messageMap.get("sessionKey");
            Integer userIdInt = (Integer) messageMap.get("userId");
            final long userId = userIdInt.longValue();

            log.warn("接收到AI聊天历史记录ID消息: type={}, sessionKey={}, userId={}", type, sessionKey, userId);
            
            // 保存会话记录
            aiChatHistoryService.save(type, sessionKey, userId);
            
            log.warn("AI聊天历史记录ID保存成功");
        } catch (Exception e) {
            log.error("保存AI聊天历史记录ID失败", e);
        }
    }
} 
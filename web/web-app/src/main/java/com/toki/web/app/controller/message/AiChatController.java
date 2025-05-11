package com.toki.web.app.controller.message;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.toki.common.result.Result;
import com.toki.web.app.service.AiChatHistoryService;
import com.toki.web.app.vo.message.MessageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.toki.common.constant.RabbitMqConstant.*;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

/**
 * 智能客服聊天控制器
 *
 * @author toki
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/ai")
@Slf4j
public class AiChatController {

    private final ChatClient serviceChatClient;
    private final AiChatHistoryService aiChatHistoryService;
    private final ChatMemory chatMemory;
    private final RabbitTemplate rabbitTemplate;

    /**
     * 聊天
     */
    @GetMapping(value = "/service", produces = "text/html;charset=UTF-8")
    public Flux<String> chat(String prompt, String chatId, Long userId) {
        // 使用userId和chatId组合成唯一标识
        String sessionKey = userId + "_" + chatId;

        // 异步保存会话ID（通过RabbitMQ）
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("type", "service");
        messageMap.put("sessionKey", sessionKey);
        messageMap.put("userId", userId);
        messageMap.put("prompt", prompt);
        rabbitTemplate.convertAndSend(MESSAGE_DIRECT, "aiMessage", messageMap);

        // 调用模型
        log.warn("AI服务调用: prompt={}, sessionKey={}", prompt, sessionKey);
        return this.serviceChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, sessionKey))
                .stream()
                .content()
                .onErrorResume(error -> {
                    // 调用服务失败回调
                    log.error("AI服务调用失败: {}", error.getMessage(), error);
                    return Flux.just("抱歉，AI服务暂时不可用，请稍后再试。");
                });
    }

    /**
     * 根据业务类型获取会话ID列表
     */
    @GetMapping("/history/{type}")
    public Result<List<String>> getChatIds(@PathVariable("type") String type, Long userId) {
        return Result.ok(aiChatHistoryService.getChatIds(type, userId));
    }

    /**
     * 根据会话ID获取会话历史记录
     */
    @GetMapping("/history/service/{chatId}")
    public Result<List<MessageVo>> getChatHistory(@PathVariable("chatId") String chatId, Long userId) {
        String sessionKey = userId + "_" + chatId;

        final List<Message> messageList = chatMemory.get(sessionKey, Integer.MAX_VALUE);
        if (CollUtil.isEmpty(messageList)) {
            return Result.ok(List.of());
        }
        // 转VoList
        return Result.ok(CglibUtil.copyList(messageList, MessageVo::new));
    }
}



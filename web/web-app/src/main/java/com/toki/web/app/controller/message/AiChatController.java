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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.toki.common.constant.RabbitMqConstant.MESSAGE_DIRECT;
import static com.toki.common.constant.SystemConstants.END_FLAG;
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
    @GetMapping(value = "/service", produces = "text/event-stream;charset=UTF-8")
//    @GetMapping(value = "/service", produces = "text/html;charset=UTF-8")
    public Flux<String> chat(String prompt, String chatId, Long userId) {
        // 使用userId和chatId组合成唯一标识作为会话ID
        String sessionId = userId + "_" + chatId;

        // todo 不应该在这保存，而是在自定义AI上下文存储类中保存
        // 异步保存会话ID记录到数据库（通过RabbitMQ）
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("type", "service");
        messageMap.put("sessionId", sessionId);
        messageMap.put("userId", userId);

//        messageMap.put("content", content.toString());
        rabbitTemplate.convertAndSend(MESSAGE_DIRECT, "aiMessage", messageMap);

        // 调用模型
        return this.serviceChatClient.prompt()
                .user(prompt)
                .advisors(
                        // 会话ID参数
                        a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, sessionId)
                )
                .stream()
                .content()
                // 用SSE长连接实现流式输出，与前端约定结束标志
                .concatWith(Flux.just(END_FLAG))
                .onErrorResume(error -> {
                    // 调用服务失败回调
                    log.error("AI服务调用失败: {}", error.getMessage(), error);
                    return Flux.just("抱歉，AI服务暂时不可用，请稍后再试。" + END_FLAG);
                });
    }

    /**
     * 根据业务类型获取会话ID列表
     */
    @GetMapping("/history/{type}")
    public Result<List<String>> getChatIds(@PathVariable("type") String type, Long userId) {
        return Result.ok(aiChatHistoryService.getSessionIds(type, userId));
    }

    /**
     * 根据会话ID获取会话历史记录
     */
    @GetMapping("/history/service/{chatId}")
    public Result<List<MessageVo>> getChatHistory(@PathVariable("chatId") String chatId, Long userId) {
        String sessionId = userId + "_" + chatId;

        // 从指定会话的对话中检索最新的N条消息
        final List<Message> messageList = chatMemory.get(sessionId, Integer.MAX_VALUE);
        if (CollUtil.isEmpty(messageList)) {
            return Result.ok(List.of());
        }
        // 转VoList
        return Result.ok(CglibUtil.copyList(messageList, MessageVo::new));
    }
}



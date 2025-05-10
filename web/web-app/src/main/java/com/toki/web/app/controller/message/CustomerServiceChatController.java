package com.toki.web.app.controller.message;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.toki.common.repository.ChatHistoryRepository;
import com.toki.web.app.vo.message.MessageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

/**
 * 智能客服聊天控制器
 *
 * @author toki
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/ai")
public class CustomerServiceChatController {

    private final ChatClient serviceChatClient;
    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatMemory chatMemory;

    /**
     * 聊天
     */
    @GetMapping(value = "/service", produces = "text/html;charset=UTF-8")
    public Flux<String> chat(String prompt, String chatId, Long userId) {
        // 使用userId和chatId组合成唯一标识
        String sessionKey = userId + "_" + chatId;
        // 保存会话记录
        chatHistoryRepository.save("service", sessionKey, userId);
        // 调用模型
        return this.serviceChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();
    }

    /**
     * 根据业务类型获取会话ID列表
     */
    @GetMapping("/history/{type}")
    public List<String> getChatIds(@PathVariable("type") String type, Long userId) {
        return chatHistoryRepository.getChatIds(type, userId);
    }

    /**
     * 根据会话ID获取会话历史记录
     */
    @GetMapping("/history/service/{chatId}")
    public List<MessageVo> getChatHistory(@PathVariable("chatId") String chatId, Long userId) {
        String sessionKey = userId + "_" + chatId;

        final List<Message> messageList = chatMemory.get(sessionKey, Integer.MAX_VALUE);
        if (CollUtil.isEmpty(messageList)) {
            return List.of();
        }
        // 转VoList
        return CglibUtil.copyList(messageList, MessageVo::new);
    }
}



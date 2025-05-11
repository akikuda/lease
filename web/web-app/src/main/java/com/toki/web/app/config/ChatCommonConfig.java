package com.toki.web.app.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.toki.common.constant.SystemConstants;
import com.toki.web.app.tools.LeaseTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author toki
 */
@Configuration
@RequiredArgsConstructor
public class ChatCommonConfig {

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

//    @Bean
//    public ChatClient chatClient(DashScopeChatModel model, ChatMemory chatMemory) {
//        return ChatClient.builder(model)
//                .defaultSystem("你的名字叫toki,你是一个公寓管理员，熟悉公寓的各个方面的知识")
//                .defaultAdvisors(
//                        new SimpleLoggerAdvisor(),
//                        new MessageChatMemoryAdvisor(chatMemory)
//                )
//                .build();
//    }

//    @Bean
//    public ChatClient gameChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
//        return ChatClient.builder(model)
//                .defaultSystem(SystemConstants.GAME_SYSTEM_SETTINGS)
//                .defaultAdvisors(
//                        new SimpleLoggerAdvisor(),
//                        new MessageChatMemoryAdvisor(chatMemory)
//                )
//                .build();
//    }

    @Bean
    public ChatClient serviceChatClient(OpenAiChatModel model, ChatMemory chatMemory, LeaseTools leaseTools) {
        return ChatClient.builder(model)
                .defaultSystem(SystemConstants.SERVICE_SYSTEM_SETTINGS)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .defaultTools(leaseTools)
                .build();
    }

}

package com.toki.web.app.config;

import com.toki.common.constant.SystemConstants;
import com.toki.web.app.tools.LeaseTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * AI聊天公共配置
 * @author toki
 */
@Configuration
@RequiredArgsConstructor
public class AiChatCommonConfig {

    @Bean
    public ChatMemory chatMemory() {
        // 暂时保存聊天记录到内存中
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
                // 自定义AI角色
                .defaultSystem(SystemConstants.SERVICE_SYSTEM_SETTINGS)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 安全守护顾问, 用于防止AI对话中出现敏感词汇
                        new SafeGuardAdvisor(List.of("色情","暴力","赌博","反动"), "抱歉,小贝无法回答这个问题~", 2),
                        // 记录ChatClient的请求和响应数据的顾问, 用于调试和监控AI交互
                        new SimpleLoggerAdvisor()
                )
                // 注入自定义工具类
                .defaultTools(leaseTools)
                .build();
    }

}

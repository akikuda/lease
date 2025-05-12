package com.toki.web.app.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.toki.common.constant.RabbitMqConstant.*;

/**
 * @author toki
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public MessageConverter messageConverter(){
        // 1.定义Jackson2Json消息转换器
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        // 2.配置自动创建消息id，用于识别不同消息，也可以在业务中基于ID判断是否是重复消息
        jackson2JsonMessageConverter.setCreateMessageIds(true);
        return jackson2JsonMessageConverter;
    }

    // 定义Direct交换机
    @Bean
    public DirectExchange directExchange(){
        return ExchangeBuilder.directExchange(MESSAGE_DIRECT).build();
    }

    // 定义消息队列
//    @Bean
//    public Queue messageDirectQueue(){
//        return QueueBuilder.durable(MESSAGE_DIRECT_QUEUE).build();
//    }

    // 绑定消息队列到Direct交换机
//    @Bean
//    public Binding bindingQueueToDirect(DirectExchange directExchange, Queue messageDirectQueue){
//        return BindingBuilder.bind(messageDirectQueue).to(directExchange).with(MESSAGE_KEY);
//    }

    // 定义Fanout交换机
    @Bean
    public FanoutExchange fanoutExchange(){
        return ExchangeBuilder.fanoutExchange(MESSAGE_FANOUT).build();
    }

    // 定义save消息队列
    @Bean
    public Queue saveMessageQueue(){
        return QueueBuilder.durable(SAVE_MESSAGE_QUEUE).build();
    }
    
    // 定义AI消息队列
    @Bean
    public Queue aiMessageQueue(){
        return QueueBuilder.durable(AI_MESSAGE_QUEUE).build();
    }

    // 定义send消息队列
//    @Bean
//    public Queue sendMessageQueue(){
//        return QueueBuilder.durable(SEND_MESSAGE_QUEUE).build();
//    }

    // 绑定消息队列到Fanout交换机
    @Bean
    public Binding bindingSaveQueueToFanout(FanoutExchange fanoutExchange, Queue saveMessageQueue){
        return BindingBuilder.bind(saveMessageQueue).to(fanoutExchange);
    }
    
    // 绑定AI消息队列到Direct交换机
    @Bean
    public Binding bindingAiQueueToDirect(DirectExchange directExchange, Queue aiMessageQueue){
        return BindingBuilder.bind(aiMessageQueue).to(directExchange).with(AI_MESSAGE_KEY);
    }

//    @Bean
//    public Binding bindingSendQueueToFanout(FanoutExchange fanoutExchange, Queue sendMessageQueue){
//        return BindingBuilder.bind(sendMessageQueue).to(fanoutExchange);
//    }
}

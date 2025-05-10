package com.toki.web.app.mq;

import com.toki.model.entity.MessageInfo;
import com.toki.web.app.service.MessageInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.toki.common.constant.RabbitMqConstant.SAVE_MESSAGE_QUEUE;

/**
 * 保存消息的消费者
 * @author toki
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class  MessageConsumer {

    private final MessageInfoService msgService;

    // 监听save消息队列
    @RabbitListener(queues = SAVE_MESSAGE_QUEUE)
    public void handelSendMessage(MessageInfo message){
        log.warn("handelSendMessage接收到消息：{}", message);
        msgService.sendMsg(message);
        log.warn("handelSendMessage处理完成");
    }
}

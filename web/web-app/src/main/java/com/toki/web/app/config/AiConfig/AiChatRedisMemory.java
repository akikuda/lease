package com.toki.web.app.config.AiConfig;

import cn.hutool.extra.cglib.CglibUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toki.model.entity.AiChatHistory;
import com.toki.web.app.service.AiChatHistoryService;
import com.toki.web.app.vo.message.MessageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.toki.common.constant.RedisConstant.AI_MEMORY_KEY;
import static com.toki.common.constant.RedisConstant.AI_MEMORY_TTL;

/**
 * 自定义AI上下文存储，基于Redis实现对话记忆存储
 *
 * @author toki
 * @version 1.0
 * @since 2025/5/7 14:51
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AiChatRedisMemory implements ChatMemory {

    private final RedisTemplate<String, Object> redisTemplate;
    private final AiChatHistoryService aiChatHistoryService;

    // add()方法的调用时机是在用户输入消息或者AI生成回复之后
    // 需要将这些消息保存起来以便后续使用。
    @Override
    public void add(String conversationId, List<Message> messages) {

        // 从会话ID中截取出用户ID作为key
//        String userId = conversationId.split("_")[0];
//        String key = AI_MEMORY_KEY + userId + ":" + conversationId;
        String key = AI_MEMORY_KEY + conversationId;

        // 转VO对象列表
        List<MessageVo> messageVoList = new ArrayList<>();
//        List<AiChatHistory> aiChatHistoryList = new ArrayList<>();
        for (Message msg : messages) {
            MessageVo messageVo = new MessageVo(msg);
            messageVo.setSessionId(conversationId);

            messageVoList.add(messageVo);

            // messageVO转AiChatHistory对象，保存到数据库
//            AiChatHistory aiChatHistory = new AiChatHistory();
//            aiChatHistory.setType(messageVo.getRole());
//            CglibUtil.copy(messageVo, aiChatHistory);
//
//            aiChatHistoryList.add(aiChatHistory);
        }
        // 使用list结构模拟队列存储消息，维护顺序
        redisTemplate.opsForList().rightPushAll(key, messageVoList.toArray());
        redisTemplate.expire(key, AI_MEMORY_TTL, TimeUnit.MINUTES);
        // todo 同时保存到数据库,需要修改表结构(增加role字段 -> 用type字段代替)，适应messageVO对象
//        aiChatHistoryService.saveBatch(aiChatHistoryList);
    }


    // get()方法的调用时机是在AI准备生成回复之前，需要获取对话的历史记录。
    // 调用效果是将prompt消息拼装最近的lastN条消息，作为AI的回复
    @Override
    public List<Message> get(String conversationId, int lastN) {
        String key = AI_MEMORY_KEY + conversationId;
        Long size = redisTemplate.opsForList().size(key);
        if (size == null || size == 0) {
            // todo 如果Redis中没有数据，则从数据库中获取历史消息，并存入Redis;若数据库也没有，返回空列表
            return Collections.emptyList();
        }

        // 从Redis中start位置开始获取消息列表，并转换为Message对象列表
        int start = Math.max(0, (int) (size - lastN));
        List<Object> listTmp = redisTemplate.opsForList().range(key, start, -1);
        List<Message> listOut = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Object obj : listTmp) {

            MessageVo chatVo = objectMapper.convertValue(obj, MessageVo.class);

            if (MessageType.USER.getValue().equals(chatVo.getRole())) {
                listOut.add(new UserMessage(chatVo.getContent()));
            } else if (MessageType.ASSISTANT.getValue().equals(chatVo.getRole())) {
                listOut.add(new AssistantMessage(chatVo.getContent()));
            } else if (MessageType.SYSTEM.getValue().equals(chatVo.getRole())) {
                listOut.add(new SystemMessage(chatVo.getContent()));
            }
        }
        return listOut;
    }

    @Override
    public void clear(String conversationId) {
        // 从会话ID中截取出用户ID作为key
//        String userId = conversationId.split("_")[0];
//        String key = AI_MEMORY_KEY + userId + ":" + conversationId;
        String key = AI_MEMORY_KEY + conversationId;
        redisTemplate.delete(key);
    }
}
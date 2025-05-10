package com.toki.common.repository;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 将AI会话记录保存到内存中
 *
 * @author toki
 */
@Component
public class InMemoryChatHistoryRepositoryImpl implements ChatHistoryRepository {

    private final HashMap<String, List<String>> chatHistory = new HashMap<>();

    @Override
    public void save(String type, String sessionKey, Long userId) {

        final List<String> chatIds = chatHistory
                .computeIfAbsent(type + "_" + userId, k -> new ArrayList<>());
        // 若已存在，则结束，避免id重复保存
        if (chatIds.contains(sessionKey)) {
            return;
        }
        chatIds.add(sessionKey);
    }

    @Override
    public List<String> getChatIds(String type, Long userId) {
        // 若不存在type对应的key，则返回空列表,List.of() 会返回一个不可变的空列表
        return chatHistory.getOrDefault(type + "_" + userId, List.of());
    }
}

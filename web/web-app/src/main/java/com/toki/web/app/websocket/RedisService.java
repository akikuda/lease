package com.toki.web.app.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.toki.common.constant.RedisConstant.*;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public synchronized int getLoginCount() {
        final String loginCount = stringRedisTemplate.opsForValue().get(LOGIN_COUNT_KEY);
        return loginCount == null ? 0 : Integer.parseInt(loginCount);
    }

    public synchronized void addLoginCount() {
        stringRedisTemplate.opsForValue().increment(LOGIN_COUNT_KEY, 1);
    }

    public synchronized void reduceLoginCount() {
        stringRedisTemplate.opsForValue().decrement(LOGIN_COUNT_KEY, 1);
    }

    public void setUserOnline(Long userId) {
        stringRedisTemplate.opsForSet().add(ONLINE_USERS_KEY, userId.toString());
    }

    public void setUserOffline(Long userId) {
        stringRedisTemplate.opsForSet().remove(ONLINE_USERS_KEY, userId.toString());
    }

    public boolean isUserOnline(Long userId) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(ONLINE_USERS_KEY, userId.toString()));
    }

    public Map<String, String> getOnlineUsers() {
        return Objects.requireNonNull(stringRedisTemplate.opsForSet().members(ONLINE_USERS_KEY))
                .stream()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
    }

    public void clearUserOnlineStatus() {
        stringRedisTemplate.delete(ONLINE_USERS_KEY);
    }

    public void clearLoginCount() {
        stringRedisTemplate.delete(LOGIN_COUNT_KEY);
    }

    /**
     * 检查消息是否已处理
     *
     * @param messageId 消息ID
     * @return 如果消息已处理返回 true，否则返回 false
     */
    public boolean isMessageProcessed(String messageId) {
        return stringRedisTemplate.hasKey(MESSAGE_PROCESSED + messageId);
    }

    /**
     * 标记消息为已处理
     *
     * @param messageId 消息ID
     */
    public void markMessageAsProcessed(String messageId) {
        stringRedisTemplate.opsForValue().set(MESSAGE_PROCESSED + messageId, "1");
    }
}

package com.toki.web.app.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.toki.common.constant.RedisConstant.*;

/**
 * 通过Redis记录在线用户并统计在线的人数
 *
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    // 获取登录人数
//    public int getLoginCount() {
//        final String loginCount = stringRedisTemplate.opsForValue().get(LOGIN_COUNT_KEY);
//        Long size = stringRedisTemplate.opsForSet().size(ONLINE_USERS_KEY);
//        if (size == null){
//            size = 0L;
//        }
//        return loginCount == null || !loginCount.equals(size.toString()) ? size.intValue() : Integer.parseInt(loginCount);
//    }
    public int getLoginCount() {
        Long size = stringRedisTemplate.opsForSet().size(ONLINE_USERS_KEY);
        return size == null ? 0 : size.intValue();
    }

    // 登录人数+1
    public void addLoginCount() {
        stringRedisTemplate.opsForValue().increment(LOGIN_COUNT_KEY, 1);
    }

    // 设置用户上线
    public void setUserOnline(Long userId) {
        stringRedisTemplate.opsForSet().add(ONLINE_USERS_KEY, userId.toString());
    }

    // 登录人数+1，并设置用户上线
    public void addLoginCountAndUserOnline(Long userId) {
        String luaScript = "redis.call('INCR', KEYS[1]) " +
                "redis.call('SADD', KEYS[2], ARGV[1]) " +
                "return 1";

        stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class),
                Arrays.asList(LOGIN_COUNT_KEY, ONLINE_USERS_KEY),
                userId.toString());
    }

    // 登录人数-1
    public void reduceLoginCount() {
        stringRedisTemplate.opsForValue().decrement(LOGIN_COUNT_KEY, 1);
    }

    // 设置用户离线
    public void setUserOffline(Long userId) {
        stringRedisTemplate.opsForSet().remove(ONLINE_USERS_KEY, userId.toString());
    }

    // 登录人数-1，并设置用户离线
    public void reduceLoginCountAndUserOffline(Long userId) {
        String luaScript = "redis.call('DECR', KEYS[1]) " +
                "redis.call('SREM', KEYS[2], ARGV[1]) " +
                "return 1";

        stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class),
                Arrays.asList(LOGIN_COUNT_KEY, ONLINE_USERS_KEY),
                userId.toString());
    }

    // 判断指定用户是否在线
    public boolean isUserOnline(Long userId) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(ONLINE_USERS_KEY, userId.toString()));
    }

    public Map<String, String> getOnlineUsers() {
        return Objects.requireNonNull(stringRedisTemplate.opsForSet().members(ONLINE_USERS_KEY))
                .stream()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
    }

    // 清除用户在线状态
    public void clearUserOnlineStatus() {
        stringRedisTemplate.delete(ONLINE_USERS_KEY);
    }

    // 清除登录人数
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

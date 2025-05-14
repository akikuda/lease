package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.AiChatHistory;
import com.toki.web.app.mapper.AiChatHistoryMapper;
import com.toki.web.app.service.AiChatHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.toki.common.constant.RedisConstant.AI_CHAT_KEY;
import static com.toki.common.constant.RedisConstant.AI_CHAT_TTL;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AiChatHistoryServiceImpl extends ServiceImpl<AiChatHistoryMapper, AiChatHistory>
        implements AiChatHistoryService {

    private final RedisTemplate<String, String> redisTemplate;
    private final AiChatHistoryMapper aiChatHistoryMapper;

    /**
     * 保存会话内容
     * */
    @Override
    public void save(String type, String sessionId, Long userId, String content) {
        final String redisKey = getRedisKey(type, userId, sessionId);
        try {
            // 使用Redis事务保证操作原子性
            redisTemplate.execute(new SessionCallback<>() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    // 添加到Redis
                    operations.opsForSet().add(redisKey, content);
                    // 设置过期时间
                    operations.expire(redisKey, AI_CHAT_TTL);
                    // 执行事务
                    return operations.exec();
                }
            });
            // 同时保存到数据库
            saveToDatabase(type, sessionId, userId, content);
        } catch (Exception e) {
            log.error("保存AI聊天历史到Redis失败: type={}, sessionId={}, userId={}", type, sessionId, userId, e);
            // 发生异常时仍尝试保存到数据库
            saveToDatabase(type, sessionId, userId, content);
        }
    }

    /**
     * 保存会话ID
     * */
    @Override
    public void save(String type, String sessionId, Long userId) {
        final String redisKey = getRedisKey2(type, userId);

        try {
            // 保存到Redis
            saveSessionIdToRedis(sessionId, redisKey);
            // 同时保存到数据库
            saveSessionIdToDatabase(type, sessionId, userId, "");

        } catch (Exception e) {
            log.error("保存AI会话ID到Redis失败: type={}, sessionId={}, userId={}", type, sessionId, userId, e);
            // 发生异常时仍尝试保存到数据库
            saveSessionIdToDatabase(type, sessionId, userId, "");
        }
    }

    private void saveSessionIdToRedis(String sessionId, String redisKey) {
        // 使用Redis事务保证操作原子性
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // 检查是否已存在
                if (Boolean.FALSE.equals(operations.opsForSet().isMember(redisKey, sessionId))) {
                    operations.multi();
                    // 添加到Redis
                    operations.opsForSet().add(redisKey, sessionId);
                    // 设置过期时间
                    operations.expire(redisKey, AI_CHAT_TTL);
                    // 执行事务
                    return operations.exec();
                }
                return null;
            }
        });
    }

    @Override
    public List<String> getSessionIds(String type, Long userId) {
        // 前缀
        final String prefixKey = getRedisKey2(type, userId);
        try {
            final Set<String> redisResult = redisTemplate.keys(prefixKey + "*");
            if (!redisResult.isEmpty()) {
                // 取出所有会话ID
                List<String> sessionIds = new ArrayList<>(redisResult);
                // 去掉前缀
                sessionIds.replaceAll(s -> s.replace(prefixKey, ""));
                return sessionIds;
            }
        } catch (Exception e) {
            log.error("从 Redis 获取AI会话ID列表失败: type={}, userId={}", type, userId, e);
        }

        // Redis没有数据或发生异常时，从数据库获取
        return getSessionId(type, userId);
    }

    /**
     * 生成Redis键
     *
     * @param type      业务类型
     * @param userId    用户ID，用于区分不同用户
     * @param sessionId 会话ID，用于区分不同会话，同一用户可能有多个会话
     */
    private String getRedisKey(String type, Long userId, String sessionId) {
        return AI_CHAT_KEY + type + ":" + userId + ":" + sessionId;
    }

    /**
     * 生成Redis键
     */
    private String getRedisKey2(String type, Long userId) {
        return AI_CHAT_KEY + type + ":" + userId;
    }

    /**
     * 保存会话内容到数据库
     */
    private void saveToDatabase(String type, String sessionId, Long userId, String content) {
        try {
            // 直接保存
            AiChatHistory aiChatHistory = new AiChatHistory()
                    .setType(type)
                    .setSessionId(sessionId)
                    .setUserId(userId)
                    .setContent(content);

            aiChatHistoryMapper.insert(aiChatHistory);
        } catch (Exception e) {
            log.error("保存AI聊天历史到数据库失败: type={}, sessionId={}, userId={}", type, sessionId, userId, e);
        }
    }

    /**
     * 保存会话ID到数据库
     */
    private void saveSessionIdToDatabase(String type, String sessionId, Long userId, String content) {
        try {
            // 是否存在会话
            LambdaQueryWrapper<AiChatHistory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AiChatHistory::getType, type)
                    .eq(AiChatHistory::getSessionId, sessionId)
                    .eq(AiChatHistory::getUserId, userId);

            if (aiChatHistoryMapper.selectCount(queryWrapper) == 0) {
            // 不存在则插入
            // 直接保存
            AiChatHistory aiChatHistory = new AiChatHistory()
                    .setType(type)
                    .setSessionId(sessionId)
                    .setUserId(userId)
                    .setContent(content);

            aiChatHistoryMapper.insert(aiChatHistory);
            }
        } catch (Exception e) {
            log.error("保存AI会话ID到数据库失败: type={}, sessionId={}, userId={}", type, sessionId, userId, e);
        }
    }

    /**
     * 从数据库获取会话ID
     */
    private List<String> getSessionId(String type, Long userId) {
        try {
            // 使用自定义方法查询
            return aiChatHistoryMapper.selectSessionKeysByTypeAndUserId(type, userId);
        } catch (Exception e) {
            log.error("从 数据库 获取AI会话ID列表失败: type={}, userId={}", type, userId, e);
            return List.of();
        }
    }
}

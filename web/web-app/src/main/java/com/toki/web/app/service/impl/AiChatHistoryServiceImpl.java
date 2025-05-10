package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.toki.common.constant.RedisConstant.AI_CHAT_HISTORY_ID_KEY;
import static com.toki.common.constant.RedisConstant.AI_CHAT_HISTORY_ID_TTL;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AiChatHistoryServiceImpl implements AiChatHistoryService {

    private final RedisTemplate<String, String> redisTemplate;
    private final AiChatHistoryMapper aiChatHistoryMapper;

    @Override
    public void save(String type, String sessionKey, Long userId) {
        final String redisKey = getRedisKey(type, userId);
        
        try {
            // 使用Redis事务保证操作原子性
            redisTemplate.execute(new SessionCallback<>() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    // 检查是否已存在
                    if (Boolean.FALSE.equals(operations.opsForSet().isMember(redisKey, sessionKey))) {
                        operations.multi();
                        // 添加到Redis
                        operations.opsForSet().add(redisKey, sessionKey);
                        // 设置过期时间
                        operations.expire(redisKey, AI_CHAT_HISTORY_ID_TTL);
                        // 执行事务
                        return operations.exec();
                    }
                    return null;
                }
            });
            
            // 同时保存到数据库
            saveToDatabase(type, sessionKey, userId);
            
        } catch (Exception e) {
            log.error("保存AI聊天历史到Redis失败: type={}, sessionKey={}, userId={}", type, sessionKey, userId, e);
            // 发生异常时仍尝试保存到数据库
            saveToDatabase(type, sessionKey, userId);
        }
    }

    @Override
    public List<String> getChatIds(String type, Long userId) {

        final String redisKey = getRedisKey(type, userId);
        
        try {
            // 优先从Redis获取
            List<String> redisResult = new ArrayList<>(Objects.requireNonNull(
                    redisTemplate.opsForSet().members(redisKey)));
            
            if (!redisResult.isEmpty()) {
                return redisResult;
            }
        } catch (Exception e) {
            log.error("从Redis获取AI聊天历史失败: type={}, userId={}", type, userId, e);
        }
        
        // Redis没有数据或发生异常时，从数据库获取
        return getFromDatabase(type, userId);
    }
    
    /**
     * 生成Redis键
     */
    private String getRedisKey(String type, Long userId) {
        return AI_CHAT_HISTORY_ID_KEY + type + ":" + userId;
    }
    
    /**
     * 保存到数据库
     */
    private void saveToDatabase(String type, String sessionKey, Long userId) {
        try {
            // 是否存在会话
            LambdaQueryWrapper<AiChatHistory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AiChatHistory::getType, type)
                       .eq(AiChatHistory::getSessionKey, sessionKey)
                       .eq(AiChatHistory::getUserId, userId);
            
            if (aiChatHistoryMapper.selectCount(queryWrapper) == 0) {
                // 不存在则插入
                AiChatHistory aiChatHistory = new AiChatHistory()
                        .setType(type)
                        .setSessionKey(sessionKey)
                        .setUserId(userId);

                aiChatHistoryMapper.insert(aiChatHistory);
            }
        } catch (Exception e) {
            log.error("保存AI聊天历史到数据库失败: type={}, sessionKey={}, userId={}", type, sessionKey, userId, e);
        }
    }
    
    /**
     * 从数据库获取
     */
    private List<String> getFromDatabase(String type, Long userId) {
        try {
            // 使用自定义方法查询
            return aiChatHistoryMapper.selectSessionKeysByTypeAndUserId(type, userId);
        } catch (Exception e) {
            log.error("从数据库获取AI聊天历史失败: type={}, userId={}", type, userId, e);
            return List.of();
        }
    }
}

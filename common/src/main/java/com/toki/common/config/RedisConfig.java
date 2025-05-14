package com.toki.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 自定义Redis配置类
 * @author toki
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> stringObjectRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 设置key和value的序列化器分别为String和JSON序列化器
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
//        template.setValueSerializer(RedisSerializer.string());

        // 设置hash的key和value的序列化器分别为String和JSON序列化器
        template.setHashKeySerializer(RedisSerializer.string());
        // GenericJackson2JsonRedisSerializer不存储类信息，反序列化时需要手动进行类型转换，适合简单类型。
        // Jackson2JsonRedisSerializer存储类信息，反序列化时不需要手动进行类型转换。
        // 奇怪，查看存储内容，发现还是有类信息？
        template.setHashValueSerializer(RedisSerializer.json());

        // 初始化
        template.afterPropertiesSet();
        return template;
    }
}
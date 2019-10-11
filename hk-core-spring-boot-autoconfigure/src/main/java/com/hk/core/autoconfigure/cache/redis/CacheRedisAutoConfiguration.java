package com.hk.core.autoconfigure.cache.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hk.commons.util.JsonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;


/**
 * @author kevin
 * @date 2018-05-31 17:45
 */
@Configuration
@ConditionalOnClass(RedisTemplate.class)
public class CacheRedisAutoConfiguration {

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setEnableTransactionSupport(true);
        RedisSerializer<String> serializer = RedisSerializer.string();
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .build();
        JsonUtils.configure(objectMapper);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL); // 会写入类名
        GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(redisSerializer);

        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        return redisTemplate;

    }
}

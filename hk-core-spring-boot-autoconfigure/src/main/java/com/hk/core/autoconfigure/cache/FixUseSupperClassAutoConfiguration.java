package com.hk.core.autoconfigure.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hk.commons.util.JsonUtils;
import com.hk.core.cache.LogCacheErrorHandler;
import com.hk.core.cache.spring.FixUseSupperClassAnnotationParser;
import com.hk.core.cache.spring.FixUseSupperClassCacheOperationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Duration;

/**
 * @author kevin
 */
@Configuration
@EnableCaching
@ConditionalOnClass(FixUseSupperClassCacheOperationSource.class)
@EnableAspectJAutoProxy(exposeProxy = true)
public class FixUseSupperClassAutoConfiguration extends CachingConfigurerSupport {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheOperationSource cacheOperationSource() {
        return new FixUseSupperClassCacheOperationSource(new FixUseSupperClassAnnotationParser());
    }

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public CacheManager cacheManager() {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory);
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .defaultUseWrapper(true)
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        JsonUtils.configure(objectMapper, true);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        RedisSerializationContext.SerializationPair<Object> serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(serializationPair);
//        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofHours(2));//缓存过期时间，默认为2小时
//        RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
//        redisCacheManager.setTransactionAware(true);
//        return redisCacheManager;
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }

    /**
     * 缓存错误处理器
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new LogCacheErrorHandler();
    }
}

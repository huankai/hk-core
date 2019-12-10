package com.hk.core.autoconfigure.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.commons.util.JsonUtils;
import com.hk.core.cache.LogCacheErrorHandler;
import com.hk.core.cache.interceptor.LockCacheInterceptor;
import com.hk.core.cache.redis.CustomRedisCacheManager;
import com.hk.core.cache.spring.FixUseSupperClassAnnotationParser;
import com.hk.core.cache.spring.FixUseSupperClassCacheOperationSource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;


/**
 * 使用 Redis 缓存
 *
 * @author kevin
 */
@Configuration
@EnableCaching
@ConditionalOnClass(FixUseSupperClassCacheOperationSource.class)
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableConfigurationProperties(value = {CacheProperties.class, NullCacheProperties.class})
public class FixUseSupperClassAutoConfiguration extends CachingConfigurerSupport {

    /**
     * @see org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration#determineConfiguration(ClassLoader)
     */
    private final CacheProperties cacheProperties;

    private final NullCacheProperties nullCacheProperties;

    private final RedisConnectionFactory redisConnectionFactory;

    public FixUseSupperClassAutoConfiguration(RedisConnectionFactory redisConnectionFactory, CacheProperties cacheProperties, NullCacheProperties nullCacheProperties) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.cacheProperties = cacheProperties;
        this.nullCacheProperties = nullCacheProperties;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheOperationSource cacheOperationSource() {
        return new FixUseSupperClassCacheOperationSource(new FixUseSupperClassAnnotationParser());
    }

    @Override
    public CacheManager cacheManager() {
        var redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory);
        var objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .build();
        JsonUtils.configure(objectMapper);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);// 会写入类名
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        RedisSerializationContext.SerializationPair<Object> serializationPair = RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        var config = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(serializationPair);
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        var cacheManager = new CustomRedisCacheManager(redisCacheWriter, config);
        cacheManager.setNullValueTtl(nullCacheProperties.getNullValueTtl());
        return cacheManager;
    }

    /**
     * 缓存错误处理器
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new LogCacheErrorHandler();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheInterceptor cacheInterceptor() {
        var interceptor = new LockCacheInterceptor();
        interceptor.configure(this::errorHandler, this::keyGenerator, this::cacheResolver, this::cacheManager);
        interceptor.setCacheOperationSource(cacheOperationSource());
        return interceptor;
    }
}

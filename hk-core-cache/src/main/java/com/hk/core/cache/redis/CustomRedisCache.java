package com.hk.core.cache.redis;

import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

/**
 * @author huangkai
 * @date 2019-02-27 15:01
 */
public class CustomRedisCache extends RedisCache {

    /**
     * null 值的缓存过期时间
     */
    private final Duration nullValueTtl;

    private final RedisCacheWriter cacheWriter;

    private final RedisCacheConfiguration cacheConfig;

    /**
     * Create new {@link RedisCache}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    CustomRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig, Duration nullValueTtl) {
        super(name, cacheWriter, cacheConfig);
        this.nullValueTtl = nullValueTtl;
        this.cacheConfig = cacheConfig;
        this.cacheWriter = cacheWriter;
    }

    @Override
    public void put(Object key, Object value) {
        Object cacheValue = preProcessCacheValue(value);
        if (!isAllowNullValues() && cacheValue == null) {
            return;
        }
        // 如果缓存值为 null ，使用 null 值的缓存过期时间
        Duration ttl = (null == cacheValue || NullValue.INSTANCE.equals(cacheValue)) ? nullValueTtl : cacheConfig.getTtl();
        cacheWriter.put(getName(), createAndConvertCacheKey(key), serializeCacheValue(cacheValue), ttl);
    }

    private byte[] createAndConvertCacheKey(Object key) {
        return serializeCacheKey(createCacheKey(key));
    }
}

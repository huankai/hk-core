package com.hk.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * <p>
 * 缓存读取出错处理器,
 * Spring 使用默认的 CacheErrorHandler 是 {@link org.springframework.cache.interceptor.SimpleCacheErrorHandler}
 * </p>
 *
 * @author: huangkai
 * @date 2018-05-15 17:20
 */
public class LogCacheErrorHandler implements CacheErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogCacheErrorHandler.class);

    /**
     * 是否抛出异常信息
     */
    private final boolean onErrorThrow;

    public LogCacheErrorHandler() {
        this(false);
    }

    public LogCacheErrorHandler(boolean onErrorThrow) {
        this.onErrorThrow = onErrorThrow;
    }

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        String message = String.format("get Error,CacheName : %s, Cache Key : %s,Error Message: %s",
                cache.getName(), String.valueOf(key), exception.getMessage());
        LOGGER.error(message);
        if (onErrorThrow) {
            throw exception;
        }
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        String message = String.format("put Error,CacheName : %s, Cache Key : %s,Error Message: %s",
                cache.getName(), String.valueOf(key),  exception.getMessage());
        LOGGER.error(message);
        if (onErrorThrow) {
            throw exception;
        }
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        String message = String.format("Evict Error,CacheName : %s, Cache Key : %s,Error Message: %s",
                cache.getName(), String.valueOf(key), exception.getMessage());
        LOGGER.error(message);
        if (onErrorThrow) {
            throw exception;
        }
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        String message = String.format("Clear Error,CacheName : %s,Error Message: %s",
                cache.getName(), exception.getMessage());
        LOGGER.error(message);
        if (onErrorThrow) {
            throw exception;
        }
    }
}

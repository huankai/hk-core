package com.hk.core.cache;

import lombok.extern.slf4j.Slf4j;
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
 * @author kevin
 * @date 2018-05-15 17:20
 */
@Slf4j
public class LogCacheErrorHandler implements CacheErrorHandler {

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
        if (log.isErrorEnabled()) {
            log.error(String.format("get Error,CacheName : %s, Cache Key : %s,Error Message: %s",
                    cache.getName(), String.valueOf(key), exception.getMessage()));
        }
        if (onErrorThrow) {
            throw exception;
        }
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        if (log.isErrorEnabled()) {
            log.error(String.format("put Error,CacheName : %s, Cache Key : %s,Error Message: %s",
                    cache.getName(), String.valueOf(key), exception.getMessage()));
        }
        if (onErrorThrow) {
            throw exception;
        }
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        if (log.isErrorEnabled()) {
            log.error(String.format("Evict Error,CacheName : %s, Cache Key : %s,Error Message: %s",
                    cache.getName(), String.valueOf(key), exception.getMessage()));
        }
        if (onErrorThrow) {
            throw exception;
        }
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        if (log.isErrorEnabled()) {
            log.error(String.format("Clear Error,CacheName : %s,Error Message: %s",
                    cache.getName(), exception.getMessage()));
        }
        if (onErrorThrow) {
            throw exception;
        }
    }
}

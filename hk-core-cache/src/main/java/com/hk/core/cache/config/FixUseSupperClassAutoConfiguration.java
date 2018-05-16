package com.hk.core.cache.config;

import com.hk.core.cache.LogCacheErrorHandler;
import com.hk.core.cache.spring.FixUseSupperClassAnnotationParser;
import com.hk.core.cache.spring.FixUseSupperClassCacheOperationSource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Role;

/**
 * @author huangkai
 */
@Configuration
@EnableCaching
@EnableAspectJAutoProxy(exposeProxy = true)
public class FixUseSupperClassAutoConfiguration extends CachingConfigurerSupport {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheOperationSource cacheOperationSource() {
        return new FixUseSupperClassCacheOperationSource(new FixUseSupperClassAnnotationParser());
    }

    /**
     * 缓存错误处理器
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new LogCacheErrorHandler();
    }
}

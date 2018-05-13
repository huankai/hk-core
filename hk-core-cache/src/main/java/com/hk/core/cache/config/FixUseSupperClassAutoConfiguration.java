package com.hk.core.cache.config;

import com.hk.core.cache.spring.FixUseSupperClassAnnotationParser;
import com.hk.core.cache.spring.FixUseSupperClassCacheOperationSource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author huangkai
 */
@Configuration
public class FixUseSupperClassAutoConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheOperationSource cacheOperationSource() {
        return new FixUseSupperClassCacheOperationSource(new FixUseSupperClassAnnotationParser());
    }
}

package com.hk.core.cache.spring;

import org.springframework.cache.annotation.CacheAnnotationParser;
import org.springframework.cache.interceptor.CacheOperation;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 *
 */
public interface FixUseSupperClassCacheAnnotationParser extends CacheAnnotationParser {

    Collection<CacheOperation> parseCacheAnnotations(Class targetClass, Method method);
}

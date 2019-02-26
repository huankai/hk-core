package com.hk.core.cache.interceptor;

import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.cache.interceptor.CacheOperationInvoker;

import java.lang.reflect.Method;

/**
 * 加锁缓存拦截器，防止 缓存雪崩
 *
 * @author huangkai
 * @date 2019/2/26 22:43
 */
public class LockCacheInterceptor extends CacheInterceptor {


    @Override
    protected synchronized Object execute(CacheOperationInvoker invoker, Object target, Method method, Object[] args) {
        return super.execute(invoker, target, method, args);
    }
}

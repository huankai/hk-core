package com.hk.core.cache.service;

import org.springframework.aop.framework.AopContext;

/**
 * @author huangkai
 * @date 2019-02-26 10:40
 */
public interface CacheService {

    /**
     * 清除缓存
     */
    void cleanCache();

    /**
     * 获取当前 Proxy
     *
     * @throws ClassCastException 强制类型转换异常
     */
    @SuppressWarnings("unchecked")
    static <P> P currentProxy() throws ClassCastException {
        return (P) AopContext.currentProxy();
    }

}

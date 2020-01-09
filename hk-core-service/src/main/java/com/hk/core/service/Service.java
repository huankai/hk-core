package com.hk.core.service;

import org.springframework.aop.framework.AopContext;

/**
 * @author kevin
 * @date 2020-01-09 16:59
 */
public interface Service {

    @SuppressWarnings("unchecked")
    static <P> P currentProxy() throws ClassCastException {
        return (P) AopContext.currentProxy();
    }
}

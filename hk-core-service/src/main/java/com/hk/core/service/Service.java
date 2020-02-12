package com.hk.core.service;

import org.springframework.aop.framework.AopContext;

/**
 * @author kevin
 * @date 2020-01-09 16:59
 */
public interface Service {

    @SuppressWarnings("unchecked")
    static <T> T currentProxy(T t) throws ClassCastException {
        try {
            return (T) AopContext.currentProxy();
        } catch (IllegalStateException e) {
            return t;
        }
    }
}

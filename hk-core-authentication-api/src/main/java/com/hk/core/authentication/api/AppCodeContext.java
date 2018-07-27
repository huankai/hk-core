package com.hk.core.authentication.api;

/**
 * @author: kevin
 * @date 2018-07-12 16:25
 */
@FunctionalInterface
public interface AppCodeContext {

    AppCode getCurrentAppInfo();
}

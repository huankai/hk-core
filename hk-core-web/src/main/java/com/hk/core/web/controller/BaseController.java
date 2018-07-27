package com.hk.core.web.controller;

import com.hk.commons.util.SpringContextHolder;

/**
 * @author: kevin
 * @date 2018-6-1 21:00
 */
public abstract class BaseController {

    /**
     * 获取国际化消息
     *
     * @param code code
     * @param args args
     * @return
     */
    protected String getMessage(String code, Object... args) {
        return getMessage(code, null, args);
    }

    /**
     * 获取国际化消息
     *
     * @param code           code
     * @param defaultMessage defaultMessage
     * @param args           args
     * @return
     */
    protected String getMessage(String code, String defaultMessage, Object... args) {
        return SpringContextHolder.getMessage(code, null, args);
    }
}

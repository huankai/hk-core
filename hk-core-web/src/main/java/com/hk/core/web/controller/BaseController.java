package com.hk.core.web.controller;

import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.web.ServletContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author: huangkai
 * @date 2018-04-04 09:05
 */
public abstract class BaseController {

    /**
     *
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected ServletContextHolder servletContextHolder;

    @Autowired
    protected SecurityContext securityContext;

    @Autowired
    private MessageSource messageSource;

    /**
     * 获取当前登陆的用户信息
     *
     * @return
     */
    protected final UserPrincipal getPrincipal() {
        return securityContext.getPrincipal();
    }

    /**
     * 获取国际化名称值
     *
     * @param code
     * @return
     */
    protected final String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }


}

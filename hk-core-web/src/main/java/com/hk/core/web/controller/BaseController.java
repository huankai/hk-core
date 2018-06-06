package com.hk.core.web.controller;

import com.hk.commons.util.SpringContextHolder;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author huangkai
 * @date 2018-6-1 21:00
 */
public abstract class BaseController {

    @Autowired
    private SecurityContext securityContext;

    protected UserPrincipal getPrincipal() {
        return securityContext.getPrincipal();
    }

    protected String getMessage(String code, Object... args) {
        return SpringContextHolder.getMessage(code, null, args);
    }
}
package com.hk.core.authentication.api;

import com.hk.commons.util.SpringContextHolder;

/**
 * @author: kevin
 * @date: 2018-08-01 16:58
 */
public class SecurityContextUtils {

    private static final SecurityContext SECURITY_CONTEXT = SpringContextHolder.getBean(SecurityContext.class);

    public static UserPrincipal getPrincipal() {
        return SECURITY_CONTEXT.getPrincipal();
    }

    public static boolean isAuthenticated() {
        return SECURITY_CONTEXT.isAuthenticated();
    }
}

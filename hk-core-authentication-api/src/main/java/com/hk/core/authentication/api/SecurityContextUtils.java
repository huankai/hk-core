package com.hk.core.authentication.api;

import com.hk.commons.util.SpringContextHolder;

/**
 * @author kevin
 * @date 2018-08-01 16:58
 */
public abstract class SecurityContextUtils {

    private static final SecurityContext SECURITY_CONTEXT = SpringContextHolder.getBean(SecurityContext.class);

    /**
     * 获取当前登陆用户信息
     *
     * @return {@link UserPrincipal}
     */
    public static UserPrincipal getPrincipal() {
        return SECURITY_CONTEXT.getPrincipal();
    }

    /**
     * 判断当前用户是否有登陆
     *
     * @return 登陆返回 true,否则返回 false
     */
    public static boolean isAuthenticated() {
        return SECURITY_CONTEXT.isAuthenticated();
    }
}

package com.hk.core.authentication.api;

import com.hk.commons.util.SpringContextHolder;

/**
 * <p>
 * SecurityContextUtils
 * </p>
 *
 * @author: kevin
 * @date 2018-04-16 09:39
 * @see SecurityContext
 */
public final class SecurityContextUtils {

    private static SecurityContext getSecurityContext() {
        return SpringContextHolder.getBean(SecurityContext.class);
    }

    /**
     * @return 返回当前登陆的用户
     */
    public static UserPrincipal getPrincipal() {
        return getSecurityContext().getPrincipal();
    }
}

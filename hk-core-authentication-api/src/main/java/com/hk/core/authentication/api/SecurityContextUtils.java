package com.hk.core.authentication.api;

import com.hk.commons.util.SpringContextHolder;

/**
 * @author: huangkai
 * @date 2018-04-16 09:39
 */
public class SecurityContextUtils {

    /**
     * @return 返回当前登陆的用户
     */
    public static SecurityContext getSecurityContext() {
        return SpringContextHolder.getBean(SecurityContext.class);
    }
}

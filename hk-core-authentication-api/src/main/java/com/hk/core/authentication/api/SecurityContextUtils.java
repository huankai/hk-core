package com.hk.core.authentication.api;

import com.hk.commons.util.SpringContextHolder;

/**
 * @author: kevin
 * @date 2018-08-01 16:58
 */
public class SecurityContextUtils {

	public static UserPrincipal getPrincipal() {
		return SpringContextHolder.getBean(SecurityContext.class).getPrincipal();
	}

	public static boolean isAuthenticated() {
		return SpringContextHolder.getBean(SecurityContext.class).isAuthenticated();
	}
}

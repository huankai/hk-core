package com.hk.core.authentication.api;

/**
 * 此接口可获取当前登陆用户信息、设置与获取session信息
 * 
 * @author: kevin
 * @date 2017年10月23日下午12:50:55
 */
public interface SecurityContext {

	/**
	 * 获取当前登陆的用户
	 * 
	 * @return
	 */
	UserPrincipal getPrincipal();

	/**
	 * 当前用户是否有认证
	 * 
	 * @return
	 */
	boolean isAuthenticated();

}

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

	/**
	 * 设置Session属性，如果session不存在，不创建新的session
	 * 
	 * @param key
	 * @param value
	 */
	default void setSessionAttribute(String key, Object value) {
		setSessionAttribute(key, value, false);
	}

	/**
	 * 设置Session属性
	 * 
	 * @param key
	 * @param value
	 * @param create
	 */
	void setSessionAttribute(String key, Object value, boolean create);

	/**
	 * 获取Session属性值
	 * 
	 * @param key
	 * @return
	 */
	<T> T getSessionAttribute(String key);

	/**
	 * 删除session属性值
	 * 
	 * @param key
	 */
	void removeSessionAttribute(String key);

}

package com.hk.core.authentication.api;

/**
 * 此接口可获取当前登陆用户信息
 *
 * @author kevin
 * @date 2017年10月23日下午12:50:55
 */
public interface SecurityContext {

    /**
     * 获取当前登陆的用户
     *
     * @return {@link UserPrincipal}
     */
    UserPrincipal getPrincipal();

    /**
     * 当前用户是否有认证
     *
     * @return 认证返回true, 否则返回false
     */
    boolean isAuthenticated();

}

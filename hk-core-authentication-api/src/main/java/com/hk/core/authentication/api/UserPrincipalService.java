package com.hk.core.authentication.api;

/**
 * 根据用户id 获取用户信息
 *
 * @author huangkai
 * @date 2019-04-21 22:55
 */
public interface UserPrincipalService {

    UserPrincipal loadByUsername(String username);
}

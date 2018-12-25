package com.hk.core.authentication.shiro;

import java.util.Optional;

/**
 * @author huangkai
 * @date 2018-12-17 14:36
 */
public interface UserDetailsService {

    /**
     * 查询用户信息
     *
     * @param userName 用户名
     * @return {@link ShiroUserPrincipal}
     */
    Optional<ShiroUserPrincipal> loadUserByLoginUsername(String userName);
}

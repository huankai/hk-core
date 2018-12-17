package com.hk.core.authentication.shiro;

import org.apache.shiro.authc.Account;

import java.util.Optional;

/**
 * @author sjq-278
 * @date 2018-12-17 14:36
 */
public interface UserDetailsService {

    /**
     * 获取当前用户信息
     *
     * @param userName 用户名
     * @return {@link Account}
     * @see org.apache.shiro.authc.SimpleAccount
     */
    Optional<Account> loadUserByLoginUsername(String userName);
}

package com.hk.core.authentication.shiro;

import com.hk.core.authentication.api.UserPrincipal;

/**
 * @author: sjq-278
 * @date: 2018-12-17 14:36
 */
public interface UserDetailsService {

    UserPrincipal loadUserByLoginUsername(String userName);
}

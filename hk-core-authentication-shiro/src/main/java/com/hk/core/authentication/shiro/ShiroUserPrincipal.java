package com.hk.core.authentication.shiro;

import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import org.apache.shiro.SecurityUtils;

/**
 * @author: sjq-278
 * @date: 2018-12-14 17:45
 */
public class ShiroUserPrincipal implements SecurityContext {

    @Override
    public UserPrincipal getPrincipal() {
        return SecurityUtils.getSubject().getPrincipals().oneByType(UserPrincipal.class);
    }

    @Override
    public boolean isAuthenticated() {
        return SecurityUtils.getSubject().isAuthenticated();
    }
}

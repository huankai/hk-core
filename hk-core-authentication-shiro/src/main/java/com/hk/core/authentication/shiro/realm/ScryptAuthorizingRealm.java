package com.hk.core.authentication.shiro.realm;

import com.hk.core.authentication.shiro.UserDetailsService;
import com.hk.core.authentication.shiro.authc.credential.ScryptCredentialsMatcher;

/**
 * {scrypt}
 *
 * @author huangkai
 * @date 2018-12-25 13:52
 */
public class ScryptAuthorizingRealm extends AbstractAuthorizingRealm {

    public ScryptAuthorizingRealm(UserDetailsService userDetailsService) {
        super(userDetailsService);
        setCredentialsMatcher(ScryptCredentialsMatcher.getInstance());
    }
}

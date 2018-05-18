package com.hk.core.authentication.security;

import com.hk.commons.util.AssertUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author huangkai
 */
public abstract class AbstractUserDetailService implements UserDetailsService {

    @Override
    public final UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AssertUtils.notBlank(username, "username must not be null");
        return loadUserByLoginUsername(username);
    }

    protected abstract SecurityUserPrincipal loadUserByLoginUsername(String username);

}

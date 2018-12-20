package com.hk.core.authentication.security;

import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 使用 Spring Security 获取当前登陆用户信息
 *
 * @author kevin
 */
public final class SpringSecurityContext implements SecurityContext {

    @Override
    public UserPrincipal getPrincipal() {
        if (!isAuthenticated()) {
            throw new AuthenticationServiceException("未认证的用户");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UserPrincipal.class.cast(authentication.getPrincipal());
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return null != authentication && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

}

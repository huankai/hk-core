package com.hk.core.authentication.security;

import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author huangkai
 */
public class SpringSecurityContext implements SecurityContext {

    @Override
    public UserPrincipal getPrincipal() {
        if (!isAuthenticated()) {
            throw new AuthenticationServiceException("未认证的用户");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return null != authentication && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    @Override
    public void setSessionAttribute(String key, Object value, boolean create) {
    }

    @Override
    public <T> T getSessionAttribute(String key) {
        return null;
    }

    @Override
    public void removeSessionAttribute(String key) {
    }

}

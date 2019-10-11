package com.hk.core.authentication.shiro;

import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import java.util.Objects;
import java.util.Optional;

/**
 * Shiro SecurityContext
 *
 * @author huangkai
 * @date 2018-12-14 17:45
 */
public class ShiroSecurityContext implements SecurityContext {

    @Override
    public UserPrincipal getPrincipal() {
        return SecurityUtils.getSubject().getPrincipals().oneByType(UserPrincipal.class);
    }

    @Override
    public boolean isAuthenticated() {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    @Override
    public <T> Optional<T> getSessionAttribute(String key, Class<T> clazz) {
        return Optional.ofNullable(clazz.cast(SecurityUtils.getSubject().getSession().getAttribute(key)));
    }

    @Override
    public void setSessionAttribute(String key, Object value, boolean create) {
        Session session = SecurityUtils.getSubject().getSession(create);
        if (Objects.nonNull(session)) {
            session.setAttribute(key, value);
        }
    }

    @Override
    public void removeSessionAttribute(String key) {
        SecurityUtils.getSubject().getSession().removeAttribute(key);
    }
}

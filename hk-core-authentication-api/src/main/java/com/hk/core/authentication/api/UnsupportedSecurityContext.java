package com.hk.core.authentication.api;

import java.util.Optional;

/**
 * @author huangkai
 * @date 2019-4-9 13:44
 */
public final class UnsupportedSecurityContext implements SecurityContext {

    @Override
    public UserPrincipal getPrincipal() {
        throw new UnsupportedOperationException("UnsupportedOperation");
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public <T> Optional<T> getSessionAttribute(String key, Class<T> clazz) throws ClassCastException {
        throw new UnsupportedOperationException("UnsupportedOperation getSessionAttribute");
    }

    @Override
    public <T> Optional<T> consumeSessionAttribute(String key, Class<T> clazz) throws ClassCastException {
        throw new UnsupportedOperationException("UnsupportedOperation consumeSessionAttribute");
    }

    @Override
    public void setSessionAttribute(String key, Object value) {
        throw new UnsupportedOperationException("UnsupportedOperation setSessionAttribute");
    }

    @Override
    public void setSessionAttribute(String key, Object value, boolean create) {
        throw new UnsupportedOperationException("UnsupportedOperation setSessionAttribute");
    }

    @Override
    public void removeSessionAttribute(String key) {
        throw new UnsupportedOperationException("UnsupportedOperation removeSessionAttribute");
    }
}

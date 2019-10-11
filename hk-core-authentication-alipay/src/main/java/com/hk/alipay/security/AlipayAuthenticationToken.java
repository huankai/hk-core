package com.hk.alipay.security;

import com.hk.core.authentication.api.UserPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 支付宝
 *
 * @author huangkai
 */
@SuppressWarnings("serial")
public class AlipayAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    public AlipayAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    public AlipayAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public String getName() {
        Object principal = getPrincipal();
        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getAccount();
        }
        return super.getName();
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}

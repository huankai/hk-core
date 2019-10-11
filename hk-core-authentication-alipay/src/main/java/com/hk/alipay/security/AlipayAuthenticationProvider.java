package com.hk.alipay.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author huangkai
 * @date 2019/3/5 17:50
 */
public class AlipayAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return new AlipayAuthenticationToken(authentication.getPrincipal(), null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AlipayAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

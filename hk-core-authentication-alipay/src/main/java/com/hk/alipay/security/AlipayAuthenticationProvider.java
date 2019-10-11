package com.hk.alipay.security;

import com.hk.commons.util.AssertUtils;
import com.hk.core.authentication.api.PostAuthenticationHandler;
import com.hk.core.authentication.api.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author huangkai
 * @date 2019/3/5 17:50
 */
@RequiredArgsConstructor
public class AlipayAuthenticationProvider implements AuthenticationProvider {

    private final PostAuthenticationHandler<UserPrincipal, UserPrincipal> authenticationHandler;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserPrincipal principal = authenticationHandler.handler((UserPrincipal) authentication.getPrincipal());
        AssertUtils.notNull(principal, "principal Must not be null.");
        return new AlipayAuthenticationToken(principal, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AlipayAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

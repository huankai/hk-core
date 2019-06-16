package com.hk.alipay.security;

import com.hk.core.authentication.api.PostAuthenticaionHandler;
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

    private final PostAuthenticaionHandler<UserPrincipal, UserPrincipal> authenticaionHandler;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserPrincipal principal = authenticaionHandler.handler((UserPrincipal) authentication.getPrincipal());
        return new AlipayAuthenticationToken(principal, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AlipayAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

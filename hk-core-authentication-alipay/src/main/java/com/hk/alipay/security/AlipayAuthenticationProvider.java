package com.hk.alipay.security;

import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.api.UserPrincipalService;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author huangkai
 * @date 2019/3/5 17:50
 */
@NoArgsConstructor
public class AlipayAuthenticationProvider implements AuthenticationProvider {

    @Setter
    private UserPrincipalService userPrincipalService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if (null != userPrincipalService) {
            principal = userPrincipalService.athenticationSuccess(principal);
        }
        return new AlipayAuthenticationToken(principal, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AlipayAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

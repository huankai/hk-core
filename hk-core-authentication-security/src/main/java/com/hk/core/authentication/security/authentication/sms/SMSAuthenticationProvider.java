package com.hk.core.authentication.security.authentication.sms;

import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.api.UserPrincipalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 短信验证 Provider
 *
 * @author kevin
 * @date 2018-07-26 16:41
 */
@RequiredArgsConstructor
public class SMSAuthenticationProvider implements AuthenticationProvider {

    private final UserPrincipalService<UserPrincipal, String> userPrincipalService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SMSAuthenticationToken token = (SMSAuthenticationToken) authentication;
        UserPrincipal principal = userPrincipalService.processAuthentication(token.getPrincipal().toString());
        SMSAuthenticationToken authenticationToken = new SMSAuthenticationToken(principal, null);
        authenticationToken.setDetails(token.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SMSAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

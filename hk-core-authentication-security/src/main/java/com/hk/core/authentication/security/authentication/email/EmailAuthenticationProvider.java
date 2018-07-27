package com.hk.core.authentication.security.authentication.email;

import com.hk.core.authentication.security.authentication.sms.SMSAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.Collections;

/**
 * 邮箱验证 Provider
 *
 * @author: kevin
 * @date 2018-07-26 16:41
 */
public class EmailAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private boolean needBindAccount;

    public EmailAuthenticationProvider(boolean needBindAccount, UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.needBindAccount = needBindAccount;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EmailAuthenticationToken token = (EmailAuthenticationToken) authentication;
        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal().toString());
        if (needBindAccount && null == userDetails) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        Collection<? extends GrantedAuthority> authorities = userDetails == null ? Collections.emptyList() : userDetails.getAuthorities();
        SMSAuthenticationToken authenticationToken = new SMSAuthenticationToken(token.getPrincipal(), authorities);
        authenticationToken.setDetails(token.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

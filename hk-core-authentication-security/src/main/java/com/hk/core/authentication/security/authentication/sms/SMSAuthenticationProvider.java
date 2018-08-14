package com.hk.core.authentication.security.authentication.sms;

import com.hk.commons.util.ByteConstants;
import com.hk.core.authentication.api.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 短信验证 Provider
 *
 * @author: kevin
 * @date 2018-07-26 16:41
 */
public class SMSAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMSAuthenticationProvider.class);

    private final UserDetailsService userDetailsService;

    public SMSAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SMSAuthenticationToken token = (SMSAuthenticationToken) authentication;
        String principal = token.getPrincipal().toString();
        UserPrincipal userPrincipal = null;
        try {
            userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(principal);
        } catch (UsernameNotFoundException e) {
            LOGGER.error("用户不存在:{}", principal);
        }
        if (null == userPrincipal) {
            userPrincipal = new UserPrincipal(null, principal, false, principal, ByteConstants.NINE, principal, null, ByteConstants.NINE, null);
//            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
//        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
        SMSAuthenticationToken authenticationToken = new SMSAuthenticationToken(userPrincipal, null);
        authenticationToken.setDetails(token.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SMSAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

package com.hk.weixin.security;

import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.api.UserPrincipalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


/**
 * 微信二维码认证提供者
 *
 * @author kevin
 * @date 2018年2月8日上午11:25:39
 */
@RequiredArgsConstructor
public class WechatAuthenticationProvider implements AuthenticationProvider {

    private final UserPrincipalService<UserPrincipal, UserPrincipal> userPrincipalService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserPrincipal principal = userPrincipalService.processAuthentication((UserPrincipal) authentication.getPrincipal());
        return new WechatAuthenticationToken(principal, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

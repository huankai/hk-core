package com.hk.weixin.security;

import com.hk.commons.util.AssertUtils;
import com.hk.core.authentication.api.PostAuthenticationHandler;
import com.hk.core.authentication.api.UserPrincipal;
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

    private final PostAuthenticationHandler<UserPrincipal, UserPrincipal> authenticationHandler;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserPrincipal principal = authenticationHandler.handler((UserPrincipal) authentication.getPrincipal());
        AssertUtils.notNull(principal, "principal Must not be null.");
        return new WechatAuthenticationToken(principal, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

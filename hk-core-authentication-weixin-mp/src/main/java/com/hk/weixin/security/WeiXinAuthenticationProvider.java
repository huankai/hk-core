package com.hk.weixin.security;

import com.hk.commons.util.AssertUtils;
import com.hk.core.authentication.api.PostAuthenticationHandler;
import com.hk.core.authentication.api.UserPrincipal;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
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
public class WeiXinAuthenticationProvider implements AuthenticationProvider {

    private final PostAuthenticationHandler<UserPrincipal, WxMpUser> authenticationHandler;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserPrincipal principal = authenticationHandler.handler((WxMpUser) authentication.getPrincipal());
        AssertUtils.notNull(principal, "principal Must not be null.");
        return new WeiXinAuthenticationToken(principal, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WeiXinAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

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
        WxMpUser wxMpUser = (WxMpUser) authentication.getPrincipal();
        UserPrincipal userPrincipal;
        if (null == authenticationHandler) {
            userPrincipal = new UserPrincipal(null, wxMpUser.getNickname(), null);
        } else {
            userPrincipal = authenticationHandler.handler((WxMpUser) authentication.getPrincipal());
            AssertUtils.notNull(userPrincipal, "principal Must not be null.");
        }
        return new WeiXinAuthenticationToken(userPrincipal, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WeiXinAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

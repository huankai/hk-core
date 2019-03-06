package com.hk.core.authentication.security.handler.login;

import com.hk.core.authentication.security.accesstoken.AccessTokenContext;
import com.hk.core.authentication.security.accesstoken.TokenUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 使用 token 认证成功后，将 token 保存
 *
 * @author huangkai
 * @date 2019/3/6 18:09
 * @see com.hk.core.authentication.security.accesstoken.AccessTokenAuthenticationProvider
 */
public class AccessTokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AccessTokenContext accessTokenContext;

    public AccessTokenAuthenticationSuccessHandler(AccessTokenContext accessTokenContext) {
        this.accessTokenContext = accessTokenContext;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        accessTokenContext.storeToken(TokenUserPrincipal.class.cast(authentication.getPrincipal()));
    }
}

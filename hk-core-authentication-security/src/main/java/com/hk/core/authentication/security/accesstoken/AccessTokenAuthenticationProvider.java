package com.hk.core.authentication.security.accesstoken;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author huangkai
 * @date 2019/3/5 15:28
 */
public class AccessTokenAuthenticationProvider implements AuthenticationProvider {

    private final AccessTokenContext tokenContext;

    public AccessTokenAuthenticationProvider(AccessTokenContext tokenContext) {
        this.tokenContext = tokenContext;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = String.valueOf(authentication.getPrincipal());
        // 根据token 获取用户登陆的用户信息
        TokenUserPrincipal userToken = tokenContext.getToken(token).orElseThrow(() -> new AuthenticationServiceException("无效的Token"));
        validateToken(userToken);
        tokenContext.refreshToken(userToken);
        return new AccessTokenAuthenticationToken(userToken, null);
    }

    private void validateToken(TokenUserPrincipal token) {
        if (token.isExpire()) {
            tokenContext.removeAccessToken(token.getToken());
            throw new AuthenticationServiceException("会话已过期");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AccessTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

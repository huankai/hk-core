package com.hk.core.authentication.oauth2.provider.token;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author kevin
 * @date 2019-12-18 16:58
 */
public class ClientResourceServerTokenServices implements ResourceServerTokenServices {

    private TokenStore tokenStore;

    @Override
    public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException, InvalidTokenException {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
        if (accessToken == null) {
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        } else if (accessToken.isExpired()) {
            tokenStore.removeAccessToken(accessToken);
            throw new InvalidTokenException("Access token expired: " + accessTokenValue);
        }

        OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
        if (result == null) {
            // in case of race condition
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        }
        return result;
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return tokenStore.readAccessToken(accessToken);
    }
}

package com.hk.core.authentication.security.accesstoken;

/**
 * @author huangkai
 * @date 2019/3/5 15:28
 */
public interface AccessTokenContext {

    Token getToken(String token);

    void removeAccessToken(String token);

    void storeToken(Token token);

    void refreshToken(Token token);

}

package com.hk.core.authentication.security.accesstoken;

import java.util.Optional;

/**
 * @author huangkai
 * @date 2019/3/5 15:28
 */
public interface AccessTokenContext {

    /**
     * 获取Token
     *
     * @param token token
     * @return {@link TokenUserPrincipal}
     */
    Optional<TokenUserPrincipal> getToken(String token);

    /**
     * 删除 Token
     *
     * @param token token
     */
    void removeAccessToken(String token);

    /**
     * 保存 token
     *
     * @param token token
     */
    void storeToken(TokenUserPrincipal token);

    /**
     * 刷新 token
     *
     * @param token token
     */
    void refreshToken(TokenUserPrincipal token);

}

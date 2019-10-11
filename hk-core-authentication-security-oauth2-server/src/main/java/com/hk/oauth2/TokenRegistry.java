package com.hk.oauth2;

import com.hk.oauth2.logout.LogoutRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.List;


/**
 * token 存储
 */
public interface TokenRegistry {

    /**
     * 添加 token
     *
     * @param authentication authentication
     * @param accessToken    accessToken
     */
    void addAccessToken(OAuth2Authentication authentication, OAuth2AccessToken accessToken);

    /**
     * 删除 token
     *
     * @param authentication authentication
     * @return List<LogoutRequest>
     */
    List<LogoutRequest> destroyAccessToken(Authentication authentication);

    /**
     * 根据 sessionId 删除 token
     *
     * @param id id，sessionId
     */
    void deleteById(String id);

}

package com.hk.oauth2;

import com.hk.oauth2.logout.LogoutRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.List;

public interface TokenRegistry {

    void addAccessToken(OAuth2Authentication authentication, OAuth2AccessToken accessToken);

    List<LogoutRequest> destroyAccessToken(Authentication authentication);

    void deleteById(String id);

}

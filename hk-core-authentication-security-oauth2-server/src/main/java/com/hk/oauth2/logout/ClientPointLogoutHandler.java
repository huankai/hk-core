package com.hk.oauth2.logout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.hk.core.authentication.oauth2.utils.AccessTokenUtils;

import lombok.RequiredArgsConstructor;

/**
 * @author kevin
 * @date 2019-5-18 11:38
 */
@RequiredArgsConstructor
public class ClientPointLogoutHandler implements LogoutHandler {

    private final SingleLogoutServiceMessageHandler singleLogoutServiceMessageHandler;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        singleLogoutServiceMessageHandler.handle(AccessTokenUtils.getAccessToken(request));
    }
}

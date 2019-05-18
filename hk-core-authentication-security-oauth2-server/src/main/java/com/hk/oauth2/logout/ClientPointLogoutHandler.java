package com.hk.oauth2.logout;

import com.hk.core.authentication.oauth2.utils.AccessTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kevin
 * @date 2019-5-18 11:38
 */
@Slf4j
@RequiredArgsConstructor
public class ClientPointLogoutHandler implements LogoutHandler {

    private final SingleLogoutServiceMessageHandler singleLogoutServiceMessageHandler;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        singleLogoutServiceMessageHandler.handle(AccessTokenUtils.getAccessToken(request));
    }
}

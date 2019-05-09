package com.hk.oauth2.logout;

import com.hk.oauth2.web.support.CookieRetrievingCookieGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kevin
 * @date 2019-5-9 9:29
 */
@Slf4j
@RequiredArgsConstructor
public class TerminateSessionLogoutHandler implements LogoutHandler {

    private final CookieRetrievingCookieGenerator cookieRetrievingCookieGenerator;

//    private final CentralAuthenticationService authenticationService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String cookieValue = cookieRetrievingCookieGenerator.retrieveCookieValue(request);
//        authenticationService.destroyTicket(cookieValue);
        cookieRetrievingCookieGenerator.removeCookie(response);
    }
}

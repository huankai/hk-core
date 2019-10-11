package com.hk.core.authentication.oauth2.session;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kevin
 * @date 2019-5-10 18:58
 */
public class Oauth2UrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private final String oauth2ServerLogoutUrl;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public Oauth2UrlLogoutSuccessHandler(String oauth2ServerLogoutUrl) {
        this.oauth2ServerLogoutUrl = oauth2ServerLogoutUrl;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        redirectStrategy.sendRedirect(request, response, oauth2ServerLogoutUrl);
    }
}

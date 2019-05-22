package com.hk.core.authentication.oauth2.session;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.oauth2.utils.AccessTokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
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
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String accessToken = AccessTokenUtils.getAccessToken(request);
        if (StringUtils.isEmpty(accessToken)) {
            if (authentication instanceof OAuth2Authentication) {
                OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oAuth2Authentication.getDetails();
                accessToken = details.getTokenValue();
            }
        }
        String redirectUrl = String.format("%s?%s=%s", oauth2ServerLogoutUrl, OAuth2AccessToken.ACCESS_TOKEN, accessToken);
        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }
}

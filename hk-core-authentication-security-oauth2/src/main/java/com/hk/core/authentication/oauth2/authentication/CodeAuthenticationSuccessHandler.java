package com.hk.core.authentication.oauth2.authentication;

import com.hk.commons.JsonResult;
import com.hk.core.web.Webs;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kevin
 * @date 2020-02-12 16:58
 */
public class CodeAuthenticationSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

    /**
     * session 中存储的 oauth2ClientContext
     */
    private static final String SESSION_OAUTH2_CLIENT_CONTEXT_KEY = "scopedTarget.oauth2ClientContext";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        DefaultOAuth2ClientContext auth2ClientContext = Webs.getAttributeFromSession(SESSION_OAUTH2_CLIENT_CONTEXT_KEY, DefaultOAuth2ClientContext.class);
        Webs.writeJson(response, HttpServletResponse.SC_OK, new JsonResult<>(auth2ClientContext.getAccessToken().getValue()));
    }
}

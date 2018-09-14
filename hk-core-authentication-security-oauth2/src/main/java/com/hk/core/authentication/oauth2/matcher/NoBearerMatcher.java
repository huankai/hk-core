package com.hk.core.authentication.oauth2.matcher;

import com.hk.commons.util.StringUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author: kevin
 * @date: 2018-08-27 13:52
 */
public class NoBearerMatcher implements RequestMatcher {

    public static final NoBearerMatcher INSTANCE = new NoBearerMatcher();

    private NoBearerMatcher() {

    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return extractToken(request);
    }

    private boolean extractToken(HttpServletRequest request) {
        return extractHeaderToken(request) && StringUtils.isEmpty(request.getParameter(OAuth2AccessToken.ACCESS_TOKEN));
    }

    private boolean extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Authorization");
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                return StringUtils.isEmpty(authHeaderValue);
            }
        }
        return true;
    }
}

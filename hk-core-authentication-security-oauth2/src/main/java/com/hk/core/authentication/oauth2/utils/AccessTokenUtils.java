package com.hk.core.authentication.oauth2.utils;

import com.hk.commons.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author kevin
 * @date 2019-5-16 14:17
 */
public abstract class AccessTokenUtils {

    public static String getAccessToken(HttpServletRequest request) {
        String authorization = extractHeaderToken(request);
        return StringUtils.isEmpty(authorization) ? request.getParameter(OAuth2AccessToken.ACCESS_TOKEN) : authorization;
    }

    private static String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(HttpHeaders.AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (StringUtils.startsWithIgnoreCase(value, OAuth2AccessToken.BEARER_TYPE)) {
                return value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
            }
        }
        return null;
    }

    public static boolean isAccessTokenRequest(HttpServletRequest request) {
        return StringUtils.isNotEmpty(getAccessToken(request));
    }
}

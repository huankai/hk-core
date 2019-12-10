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

    /**
     * 从请求头或请求参数中获取 access_token 信息
     *
     * @param request request
     * @return access_token
     */
    public static String getAccessToken(HttpServletRequest request) {
        var authorization = extractHeaderToken(request);
        return StringUtils.isEmpty(authorization) ? request.getParameter(OAuth2AccessToken.ACCESS_TOKEN) : authorization;
    }

    /**
     * 从请求头获取  access_token 信息
     *
     * @param request request
     * @return access_token
     */
    private static String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(HttpHeaders.AUTHORIZATION);
        while (headers.hasMoreElements()) {
            var value = headers.nextElement();
            if (StringUtils.startsWithIgnoreCase(value, OAuth2AccessToken.BEARER_TYPE)) {
                return value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
            }
        }
        return null;
    }

    /**
     * 是否为 accessToken 请求
     *
     * @param request request
     * @return true or false
     */
    public static boolean isAccessTokenRequest(HttpServletRequest request) {
        return StringUtils.isNotEmpty(getAccessToken(request));
    }
}

package com.hk.core.autoconfigure.web;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.oauth2.utils.AccessTokenUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * accessToken 拦截器，将 AccessToken 保存到 request 上下文中
 *
 * @author kevin
 * @date 2019-06-19 09:30:05
 */
class AccessTokenInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = AccessTokenUtils.getAccessToken(request);
        if (StringUtils.isNotEmpty(accessToken)) {
            request.setAttribute("accessToken", accessToken);
        }
        return true;
    }
}

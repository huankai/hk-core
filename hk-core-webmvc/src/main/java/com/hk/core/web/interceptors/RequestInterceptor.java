package com.hk.core.web.interceptors;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.api.SecurityContext;

/**
 * 请求拦截器，设置属性到request
 *
 * @author huangkai
 * @date 2017年10月25日下午1:09:17
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    public static final String SESSION_USER_KEY = "user";

    private SecurityContext securityContext;

    private Map<String, String> properties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (CollectionUtils.isNotEmpty(properties)) {
            properties.forEach(request::setAttribute);
        }
        if (securityContext.isAuthenticated()) {
            request.setAttribute(SESSION_USER_KEY, securityContext.getPrincipal());
        }
        return true;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }
}

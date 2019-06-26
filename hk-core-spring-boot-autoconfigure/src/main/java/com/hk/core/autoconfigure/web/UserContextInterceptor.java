package com.hk.core.autoconfigure.web;

import com.hk.commons.util.ClassUtils;
import com.hk.core.authentication.api.SecurityContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 将当前用户信息放入request
 *
 * @author sjq-278
 * @date 2018-12-03 15:00
 */
public class UserContextInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (SecurityContextUtils.isAuthenticated()) {
            request.setAttribute("currentUser", SecurityContextUtils.getPrincipal());
        }
        return true;
    }
}

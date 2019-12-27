package com.hk.core.autoconfigure.web.interceptor;

import com.hk.commons.JsonResult;
import com.hk.core.authentication.api.SecurityContextUtils;
import com.hk.core.web.Webs;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kevin
 * @date 2019-12-19 14:38
 */
public class UserURIPermissionHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        if (SecurityContextUtils.isAuthenticated() && SecurityContextUtils.getPrincipal().hasPermission(requestURI)) {
            return true;
        }
        Webs.writeJson(response, HttpServletResponse.SC_OK, JsonResult.forbidden(null));
        return false;
    }
}

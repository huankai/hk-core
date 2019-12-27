package com.hk.core.authentication.security;

import com.hk.commons.JsonResult;
import com.hk.commons.util.SpringContextHolder;
import com.hk.core.web.Webs;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kevin
 * @date 2019/12/16 14:15
 */
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        Webs.writeJson(response, HttpServletResponse.SC_OK, JsonResult.unauthorized(SpringContextHolder.getMessage("user.unauthorized")));
    }
}

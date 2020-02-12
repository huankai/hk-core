package com.hk.core.authentication.security;

import com.hk.commons.JsonResult;
import com.hk.commons.Status;
import com.hk.core.web.Webs;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kevin
 * @date 2019/12/16 14:15
 */
public class JsonAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public JsonAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        String loginUrl = buildRedirectUrlToLoginPage(request, response, authException);
        Webs.writeJson(response, HttpServletResponse.SC_OK, new JsonResult<>(Status.UNAUTHORIZED, "用户未认证", loginUrl));
    }
}

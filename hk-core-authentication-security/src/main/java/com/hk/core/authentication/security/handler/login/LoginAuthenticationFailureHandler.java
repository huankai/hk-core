package com.hk.core.authentication.security.handler.login;

import com.hk.commons.JsonResult;
import com.hk.core.web.Webs;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kevin
 * @date 2018-07-26 17:29
 */
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    public LoginAuthenticationFailureHandler() {
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        Webs.writeJson(response, HttpServletResponse.SC_OK, JsonResult.badRequest(exception.getMessage()));
    }
}

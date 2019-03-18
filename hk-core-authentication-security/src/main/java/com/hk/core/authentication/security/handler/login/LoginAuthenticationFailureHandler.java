package com.hk.core.authentication.security.handler.login;

import com.hk.commons.JsonResult;
import com.hk.core.web.Webs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 如果 是 ajax 请求，android请求　，苹果app 请求，认证失败后返回 Json 数据
 *
 * @author kevin
 * @date 2018-07-26 17:29
 */
public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public LoginAuthenticationFailureHandler(String forwordUrl) {
        setDefaultFailureUrl(forwordUrl);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (Webs.isAjax(request) || Webs.isAndroid(request) || Webs.isIPhone(request)) {
            Webs.writeJson(response, HttpServletResponse.SC_OK, JsonResult.badRequest(exception.getMessage()));
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}

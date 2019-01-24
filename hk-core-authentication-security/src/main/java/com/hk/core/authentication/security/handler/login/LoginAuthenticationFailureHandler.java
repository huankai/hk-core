package com.hk.core.authentication.security.handler.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败Handler
 *
 * @author kevin
 * @date 2018-07-26 17:29
 */
public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthenticationFailureHandler.class);

    public LoginAuthenticationFailureHandler() {
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        LOGGER.info("登陆失败 : {}", exception.getMessage());
//        switch (responseType) {
//            case JSON:
//                Webs.writeJson(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), JsonResult.error(exception.getMessage()));
//                break;
//            case REDIRECT:
//            default:
//                super.onAuthenticationFailure(request, response, exception);
//                break;
//        }
    }
}

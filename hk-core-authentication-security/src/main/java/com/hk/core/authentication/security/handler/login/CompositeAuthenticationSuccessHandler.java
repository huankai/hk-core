package com.hk.core.authentication.security.handler.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 多个 认证成功后的处理器
 *
 * @author huangkai
 * @date 2019/3/6 18:08
 */
public class CompositeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final List<AuthenticationSuccessHandler> handlers;

    public CompositeAuthenticationSuccessHandler(AuthenticationSuccessHandler... handlers) {
        this.handlers = (handlers == null) ? Collections.emptyList() : Arrays.asList(handlers);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        for (AuthenticationSuccessHandler handler : handlers) {
            handler.onAuthenticationSuccess(request, response, authentication);
        }
    }
}

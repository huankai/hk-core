package com.hk.core.authentication.security.handler.login;

import com.hk.core.web.Webs;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 如果是 ajax 请求、android 请求、苹果手机应用请求，使用内部转发，因为使用重定向是客户端行为，
 * 会导致响应码为 302，可能导致客户端不能获取 Json 格式的数据
 *
 * @author kevin
 * @date 2018-07-26 17:28
 */
public class ForwardOrSaveRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public ForwardOrSaveRequestAwareAuthenticationSuccessHandler(String forwardUrl) {
        setDefaultTargetUrl(forwardUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        if (Webs.isAjax(request) || Webs.isAndroid(request) || Webs.isIPhone(request)) {
            request.getRequestDispatcher(getDefaultTargetUrl()).forward(request, response);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}

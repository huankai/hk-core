package com.hk.core.autoconfigure.exception;

import com.hk.core.web.JsonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: kevin
 * @date 2018-08-17 12:28
 */
@RestController
public class DefaultErrorController implements ErrorController {

    @Value("${security.oauth2.sso.default-failure-url:/error}")
    private String errorPath;

    @RequestMapping("${security.oauth2.sso.default-failure-url:/error}")
    public JsonResult error(HttpServletRequest request) {
        AuthenticationException exception = AuthenticationException.class.cast(request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
        if (null == exception) {
            exception = AuthenticationException.class.cast(request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
        }
        //为什么错误信息要这么拿，因为Spring 封装了两层Exception，可通过源码查看
        // OAuth2AccessTokenSupport.retrieveToken(AccessTokenRequest, OAuth2ProtectedResourceDetails, MultiValueMap<String, String>, HttpHeaders) 方法中的 OAuth2Exception 捕捉后封装为 OAuth2AccessDeniedException 一层
        // OAuth2ClientAuthenticationProcessingFilter.attemptAuthentication(HttpServletRequest, HttpServletResponse) 方法中的 OAuth2Exception 捕捉后封装为 BadCredentialsException 一层
        return JsonResult.failure(null == exception ? "未知错误！" : exception.getCause().getCause().getMessage());
    }

    @Override
    public String getErrorPath() {
        return errorPath;
    }
}

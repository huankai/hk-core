package com.hk.core.autoconfigure.exception;

import com.hk.commons.JsonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Oauth2 登陆错误处理器
 *
 * @author huangkai
 * @date 2018-12-27 15:42
 */
@RestController
public class Oauth2ErrorController {

    /**
     * Oauth2 登陆失败处理
     *
     * @param request
     * @return
     */
    @RequestMapping(path = "${hk.authentication.default-failure-url:/oauth2-error}")
    public JsonResult<Void> oauth2AuthenticationError(HttpServletRequest request) {
        AuthenticationException exception = AuthenticationException.class.cast(request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
        if (null == exception) {
            exception = AuthenticationException.class.cast(request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
        }
        //为什么错误信息要这么拿，因为Spring 封装了两层Exception，可通过源码查看
        // OAuth2AccessTokenSupport.retrieveToken(AccessTokenRequest, OAuth2ProtectedResourceDetails, MultiValueMap<String, String>, HttpHeaders) 方法中的 OAuth2Exception 捕捉后封装为 OAuth2AccessDeniedException 一层
        // OAuth2ClientAuthenticationProcessingFilter.attemptAuthentication(HttpServletRequest, HttpServletResponse) 方法中的 OAuth2Exception 捕捉后封装为 BadCredentialsException 一层
//        return JsonResult.failure(null == exception ? "未知错误！" : exception.getCause().getCause().getMessage());
        return JsonResult.failure(null == exception ? "登陆失败" : exception.getCause().getCause().getMessage());
    }
}

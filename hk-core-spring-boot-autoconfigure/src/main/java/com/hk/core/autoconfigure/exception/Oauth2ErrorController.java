package com.hk.core.autoconfigure.exception;

import com.hk.commons.JsonResult;
import com.hk.commons.util.CollectionUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
        String message = null;
        if (exception != null) {
            //为什么错误信息要这么拿，因为Spring 封装了三层Exception，可通过源码查看
            // OAuth2AccessTokenSupport.retrieveToken(AccessTokenRequest, OAuth2ProtectedResourceDetails, MultiValueMap<String, String>, HttpHeaders) 方法中的 OAuth2Exception 捕捉后封装为 OAuth2AccessDeniedException 一层
            // OAuth2ClientAuthenticationProcessingFilter.attemptAuthentication(HttpServletRequest, HttpServletResponse) 方法中的 OAuth2Exception 捕捉后封装为 BadCredentialsException 一层
            Throwable cause = exception.getCause().getCause();
            if (cause instanceof OAuth2Exception) {
                Map<String, String> additionalInformation = ((OAuth2Exception) cause).getAdditionalInformation();
                message = CollectionUtils.isEmpty(additionalInformation) ? cause.getMessage() :
                        additionalInformation.getOrDefault("message", cause.getMessage());
            } else {
                message = cause.getMessage();
            }
        } else {
            message = "未知错误";
        }
        return JsonResult.failure(message);
    }
}

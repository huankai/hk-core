package com.hk.core.authentication.security.authentication.filters;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.validatecode.ValidateCodeException;
import com.hk.core.authentication.api.validatecode.ValidateCodeProcessor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码
 *
 * @author: kevin
 * @date 2018-07-26 15:35
 */
public class ValidateCodeFilter extends OncePerRequestFilter {

    /**
     * 登陆失败处理器
     */
    private final AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 请求URL
     */
    private final String processingUri;

    /**
     * 请求方式
     */
    private RequestMethod method = RequestMethod.POST;

    /**
     * 验证处理器
     */
    private final ValidateCodeProcessor validateCodeProcessor;


    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    /**
     * @param authenticationFailureHandler 验证失败处理器
     * @param validateCodeProcessor        验证处理器
     * @param processingUri                处理URI
     */
    public ValidateCodeFilter(AuthenticationFailureHandler authenticationFailureHandler,
                              ValidateCodeProcessor validateCodeProcessor,
                              String processingUri) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.validateCodeProcessor = validateCodeProcessor;
        this.processingUri = processingUri;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod().toUpperCase();
        String requestURI = request.getRequestURI();
        if (StringUtils.equals(requestURI, processingUri) && StringUtils.equals(method, this.method.name())) {
            try {
                validate(new ServletWebRequest(request, response));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationServiceException(e.getMessage(), e));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validate(ServletWebRequest request) throws ValidateCodeException {
        validateCodeProcessor.validate(request);
    }
}

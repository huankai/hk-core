package com.hk.core.authentication.security.authentication.sms;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.validatecode.ValidateCodeProcessor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 短信登陆过滤器
 *
 * @author kevin
 * @date 2018-07-26 16:21
 */
public class SMSAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final String phoneParameter;

    private final boolean postOnly;

    private ValidateCodeProcessor validateCodeProcessor;

    public SMSAuthenticationFilter(String phoneParameter, String phoneRequestUrl, boolean postOnly,
                                   ValidateCodeProcessor validateCodeProcessor) {
        super(new AntPathRequestMatcher(phoneRequestUrl, HttpMethod.POST.name()));
        this.phoneParameter = phoneParameter;
        this.postOnly = postOnly;
        this.validateCodeProcessor = validateCodeProcessor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, ServletRequestBindingException {
        if (postOnly && StringUtils.notEquals(HttpMethod.POST.name(), request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        validateCodeProcessor.validate(new ServletWebRequest(request, response));
        String mobile = StringUtils.trimToEmpty(obtainMobile(request));
        SMSAuthenticationToken authenticationToken = new SMSAuthenticationToken(mobile);
        setDetails(request, authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    private String obtainMobile(HttpServletRequest request) {
        return request.getParameter(phoneParameter);
    }

    private void setDetails(HttpServletRequest request, SMSAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}

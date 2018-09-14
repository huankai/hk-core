package com.hk.core.authentication.security.authentication.sms;

import com.hk.commons.util.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 短信登陆过滤器
 *
 * @author: kevin
 * @date: 2018-07-26 16:21
 */
public class SMSAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final String phoneParameter;

    private final boolean postOnly;

    public SMSAuthenticationFilter(String phoneParameter,String phoneRequestUrl,boolean postOnly) {
        super(new AntPathRequestMatcher(phoneRequestUrl, HttpMethod.POST.name()));
        this.phoneParameter = phoneParameter;
        this.postOnly = postOnly;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && StringUtils.notEquals(HttpMethod.POST.name(), request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
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

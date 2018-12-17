package com.hk.core.autoconfigure.authentication.security;

import com.hk.core.authentication.api.validatecode.ValidateCodeProcessor;
import com.hk.core.authentication.security.authentication.filters.ValidateCodeFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * 验证码校验配置
 *
 * @author kevin
 * @date 2018-07-27 11:20
 */
public class ValidateCodeSecurityConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthenticationProperties.SMSProperties smsProperties;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final ValidateCodeProcessor smsCodeProcessor;

    public ValidateCodeSecurityConfiguration(AuthenticationProperties.SMSProperties smsProperties,
                                             ValidateCodeProcessor smsCodeProcessor,
                                             AuthenticationFailureHandler authenticationFailureHandler) {
        this.smsProperties = smsProperties;
        this.smsCodeProcessor = smsCodeProcessor;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    public void configure(HttpSecurity http) {
        if (smsProperties.isEnabled()) {
            ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter(smsCodeProcessor, smsProperties.getPhoneLoginUri());
            validateCodeFilter.setPostOnly(smsProperties.isPostOnly());
            if (null != authenticationFailureHandler) {
                validateCodeFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
            }
            http.addFilterBefore(validateCodeFilter, AbstractPreAuthenticatedProcessingFilter.class);
        }
    }
}
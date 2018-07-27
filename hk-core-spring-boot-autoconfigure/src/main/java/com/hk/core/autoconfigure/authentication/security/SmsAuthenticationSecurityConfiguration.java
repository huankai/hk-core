package com.hk.core.autoconfigure.authentication.security;

import com.hk.commons.sms.SmsCodeSender;
import com.hk.core.authentication.api.validatecode.DefaultGenerator;
import com.hk.core.authentication.api.validatecode.SmsCodeProcessor;
import com.hk.core.authentication.security.authentication.filters.ValidateCodeFilter;
import com.hk.core.authentication.security.authentication.sms.SMSAuthenticationFilter;
import com.hk.core.authentication.security.authentication.sms.SMSAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 短信验证配置
 *
 * @author: kevin
 * @date 2018-07-27 09:07
 */
public class SmsAuthenticationSecurityConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthenticationProperties.SMSProperties smsProperties;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final UserDetailsService userDetailsService;

    private SmsCodeSender smsCodeSender;

    public SmsAuthenticationSecurityConfiguration(AuthenticationProperties.SMSProperties smsProperties,
                                                  AuthenticationSuccessHandler successHandler,
                                                  AuthenticationFailureHandler failureHandler,
                                                  UserDetailsService userDetailsService, SmsCodeSender smsCodeSender) {
        this.smsProperties = smsProperties;
        this.authenticationSuccessHandler = successHandler;
        this.authenticationFailureHandler = failureHandler;
        this.userDetailsService = userDetailsService;
        this.smsCodeSender = smsCodeSender;

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (smsProperties.isEnabled()) {
            /* ******************* Spring Security 配置 ********************************** */
            SMSAuthenticationFilter smsAuthenticationFilter = new SMSAuthenticationFilter(smsProperties.getPhoneParameter(),
                    smsProperties.getPhoneRequestUri(), smsProperties.isPostOnly());
            smsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
            smsAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
            smsAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
            SMSAuthenticationProvider smsAuthenticationProvider = new SMSAuthenticationProvider(smsProperties.isNeedBindAccount(), userDetailsService);
            http.authenticationProvider(smsAuthenticationProvider).addFilterAfter(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            /* ******************* 短信验证码发送配置 ********************************** */
            SmsCodeProcessor smsCodeProcessor = new SmsCodeProcessor(new DefaultGenerator(smsProperties.getCodeLength(), smsProperties.getCodeExpireIn()), smsCodeSender,
                    smsProperties.getPhoneParameter(), smsProperties.getCodeParameter());
            http.apply(new ValidateCodeSecurityConfiguration(new ValidateCodeFilter(authenticationFailureHandler, smsCodeProcessor, smsProperties.getPhoneRequestUri())));
        }
    }
}

package com.hk.core.autoconfigure.authentication.security;

import com.hk.core.authentication.api.PostAuthenticationHandler;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.api.validatecode.ValidateCodeProcessor;
import com.hk.core.authentication.security.authentication.sms.SMSAuthenticationFilter;
import com.hk.core.authentication.security.authentication.sms.SMSAuthenticationProvider;
import com.hk.core.authentication.security.authentication.sms.SMSSenderFilter;
import com.hk.core.autoconfigure.authentication.AuthenticationProperties;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * 短信验证配置
 *
 * @author kevin
 * @date 2018-07-27 09:07
 */
public class SmsAuthenticationSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthenticationProperties.SMSProperties smsProperties;

    /**
     * 认证成功后的处理
     */
    @Setter
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    private ValidateCodeProcessor validateCodeProcessor;

    /**
     * 认证失败后的处理
     */
    @Setter
    private AuthenticationFailureHandler authenticationFailureHandler;

    private final PostAuthenticationHandler<UserPrincipal, String> authenticationHandler;

    public SmsAuthenticationSecurityConfigurer(AuthenticationProperties.SMSProperties smsProperties,
                                                  ValidateCodeProcessor validateCodeProcessor,
                                                  PostAuthenticationHandler<UserPrincipal, String> authenticationHandler) {
        this.smsProperties = smsProperties;
        this.validateCodeProcessor = validateCodeProcessor;
        this.authenticationHandler = authenticationHandler;

    }

    @Override
    public void configure(HttpSecurity http) {
        var smsAuthenticationFilter = new SMSAuthenticationFilter(smsProperties.getPhoneParameter(),
                smsProperties.getPhoneLoginUri(), smsProperties.isPostOnly(), validateCodeProcessor);
        smsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        if (this.authenticationSuccessHandler != null) {
            smsAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        }
        if (this.authenticationFailureHandler != null) {
            smsAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        }
        var smsAuthenticationProvider = new SMSAuthenticationProvider(authenticationHandler);
        http.addFilterBefore(new SMSSenderFilter(smsProperties.getSendUri(), validateCodeProcessor), UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(smsAuthenticationProvider).addFilterAfter(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
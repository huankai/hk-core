package com.hk.core.autoconfigure.authentication.security;

import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.api.UserPrincipalService;
import com.hk.core.authentication.security.authentication.sms.SMSAuthenticationFilter;
import com.hk.core.authentication.security.authentication.sms.SMSAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 短信验证配置
 *
 * @author kevin
 * @date 2018-07-27 09:07
 */
public class SmsAuthenticationSecurityConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthenticationProperties.SMSProperties smsProperties;

//    private final AuthenticationSuccessHandler authenticationSuccessHandler;

//    private AuthenticationFailureHandler authenticationFailureHandler;

    private final UserPrincipalService<UserPrincipal, String> smsUserPrincipalService;

    public SmsAuthenticationSecurityConfiguration(AuthenticationProperties.SMSProperties smsProperties,
                                                  UserPrincipalService<UserPrincipal, String> smsUserPrincipalService) {
        this.smsProperties = smsProperties;
//        this.authenticationSuccessHandler = successHandler;
//        this.authenticationFailureHandler = failureHandler;
        this.smsUserPrincipalService = smsUserPrincipalService;

    }

    @Override
    public void configure(HttpSecurity http) {
        if (smsProperties.isEnabled()) {
            /* ******************* Spring Security 配置 ********************************** */
            SMSAuthenticationFilter smsAuthenticationFilter = new SMSAuthenticationFilter(smsProperties.getPhoneParameter(),
                    smsProperties.getPhoneLoginUri(), smsProperties.isPostOnly());
            smsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
//            smsAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
//            smsAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
            SMSAuthenticationProvider smsAuthenticationProvider = new SMSAuthenticationProvider(smsUserPrincipalService);
            http.authenticationProvider(smsAuthenticationProvider).addFilterAfter(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }
}
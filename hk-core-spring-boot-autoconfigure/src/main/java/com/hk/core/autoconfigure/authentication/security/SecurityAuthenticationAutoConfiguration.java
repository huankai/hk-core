package com.hk.core.autoconfigure.authentication.security;

import com.hk.commons.sms.DefaultSmsCodeSender;
import com.hk.commons.sms.SmsCodeSender;
import com.hk.core.authentication.security.SpringSecurityContext;
import com.hk.core.authentication.security.handler.LoginAuthenticationFailureHandler;
import com.hk.core.authentication.security.handler.LoginAuthenticationSuccessHandler;
import com.hk.core.authentication.security.session.DefaultExpiredSessionStrategy;
import com.hk.core.authentication.security.session.DefaultInvalidSessionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;


/**
 *
 */
@Configuration
@ConditionalOnClass(value = {SpringSecurityContext.class, PasswordEncoder.class})
@EnableConfigurationProperties(AuthenticationProperties.class)
public class SecurityAuthenticationAutoConfiguration {

    @Autowired
    private AuthenticationProperties properties;

    public SecurityAuthenticationAutoConfiguration(AuthenticationProperties properties) {
        this.properties = properties;
    }

    /**
     * SecurityContext
     *
     * @return
     */
    @Bean
    public SpringSecurityContext securityContext() {
        return new SpringSecurityContext();
    }

    /**
     * 密码编码器,支持多种方式的加密
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 登陆成功处理器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AuthenticationFailureHandler failureHandler() {
        return new LoginAuthenticationFailureHandler(properties.getResponseType());
    }

    /**
     * 登陆失败处理器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    public AuthenticationSuccessHandler successHandler() {
        return new LoginAuthenticationSuccessHandler(properties.getResponseType());
    }

    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy() {
        return new DefaultInvalidSessionStrategy(properties.getBrowser().getSessionInvalidUrl());
    }


    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new DefaultExpiredSessionStrategy(properties.getBrowser().getSessionInvalidUrl());
    }


    /**
     * 默认短信发送配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender defaultSmsCodeSender() {
        return new DefaultSmsCodeSender();
    }

}

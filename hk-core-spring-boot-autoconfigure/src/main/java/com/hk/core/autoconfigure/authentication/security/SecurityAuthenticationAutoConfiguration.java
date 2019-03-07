package com.hk.core.autoconfigure.authentication.security;

import com.hk.commons.sms.DefaultSmsCodeSender;
import com.hk.commons.sms.SmsCodeSender;
import com.hk.core.authentication.api.validatecode.*;
import com.hk.core.authentication.security.SpringSecurityContext;
import com.hk.core.authentication.security.savedrequest.GateWayHttpSessionRequestCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.RequestCache;


/**
 *
 */
@Configuration
@ConditionalOnClass(value = {SpringSecurityContext.class, PasswordEncoder.class})
@EnableConfigurationProperties(AuthenticationProperties.class)
public class SecurityAuthenticationAutoConfiguration {

    private AuthenticationProperties properties;

    public SecurityAuthenticationAutoConfiguration(AuthenticationProperties properties) {
        this.properties = properties;
    }

    /**
     * SecurityContext
     *
     * @return SpringSecurityContext
     */
    @Bean
    @ConditionalOnMissingBean
    public SpringSecurityContext securityContext() {
        return new SpringSecurityContext();
    }

    /**
     * 密码编码器,支持多种方式的加密
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(prefix = "hk.authentication.login", name = "gate-way-host")
    public RequestCache requestCache() {
        return new GateWayHttpSessionRequestCache(properties.getLogin().getGateWayHost());
    }

//    /**
//     * 登陆成功处理器
//     *
//     * @return
//     */
//    @Bean
//    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
//    public AuthenticationFailureHandler failureHandler() {
//        return new LoginAuthenticationFailureHandler(properties.getResponseType());
//    }
//
//    /**
//     * 登陆失败处理器
//     *
//     * @return
//     */
//    @Bean
//    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
//    public AuthenticationSuccessHandler successHandler() {
//        return new LoginAuthenticationSuccessHandler(properties.getResponseType());
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
//    public InvalidSessionStrategy invalidSessionStrategy() {
//        return new DefaultInvalidSessionStrategy(properties.getBrowser().getSessionInvalidUrl());
//    }
//
//
//    @Bean
//    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
//    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
//        return new DefaultExpiredSessionStrategy(properties.getBrowser().getSessionInvalidUrl());
//    }


    /**
     * <p>
     * 默认短信发送配置
     * </p>
     *
     * <p>
     * 必须要在 application.yml 配置文件中配置 hk.authentication.sms.enabled = true 才会有效
     * </p>
     *
     * @return
     */
    @Configuration
    @ConditionalOnProperty(prefix = "hk.authentication.sms", name = "enabled", havingValue = "true")
    protected class SmsAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean(SmsCodeSender.class)
        public SmsCodeSender defaultSmsCodeSender() {
            return new DefaultSmsCodeSender();
        }

        @Bean("smsValidateCodeGenerator")
        @ConditionalOnMissingBean(DefaultValidateCodeGenerator.class)
        public ValidateCodeGenerator<ValidateCode> validateCodeGenerator() {
            AuthenticationProperties.SMSProperties sms = properties.getSms();
            return new DefaultValidateCodeGenerator(sms.getCodeLength(), sms.getCodeExpireIn());
        }

        @Autowired(required = false)
        private ValidateCodeStrategy validateCodeStrategy;

        @Bean("smsValidateCodeProcessor")
        public ValidateCodeProcessor validateCodeProcessor(SmsCodeSender smsCodeSender, ValidateCodeGenerator<ValidateCode> validateCodeGenerator) {
            AuthenticationProperties.SMSProperties sms = properties.getSms();
            SmsCodeProcessor smsCodeProcessor = new SmsCodeProcessor(validateCodeGenerator, smsCodeSender,
                    sms.getPhoneParameter(), sms.getCodeParameter());
            if (null != validateCodeStrategy) {
                smsCodeProcessor.setValidateCodeStrategy(validateCodeStrategy);
            }
            return smsCodeProcessor;
        }
    }

}

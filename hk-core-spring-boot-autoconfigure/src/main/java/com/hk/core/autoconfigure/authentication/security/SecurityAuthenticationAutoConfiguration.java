package com.hk.core.autoconfigure.authentication.security;

import com.hk.core.authentication.security.SpringSecurityContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 *
 */
@Configuration
@ConditionalOnClass(value = {SpringSecurityContext.class, PasswordEncoder.class})
@EnableConfigurationProperties(AuthenticationProperties.class)
public class SecurityAuthenticationAutoConfiguration {

//    @Autowired
//    private AuthenticationProperties properties;
//
//    public SecurityAuthenticationAutoConfiguration(AuthenticationProperties properties) {
//        this.properties = properties;
//    }

    /**
     * SecurityContext
     *
     * @return
     */
    @Bean
    public SpringSecurityContext securityContext() {
        return new SpringSecurityContext();
    }

//    /**
//     * 密码编码器,支持多种方式的加密
//     *
//     * @return
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

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
//
//    /**
//     * 默认短信发送配置
//     *
//     * @return
//     */
//    @Bean
//    @ConditionalOnMissingBean(SmsCodeSender.class)
//    public SmsCodeSender defaultSmsCodeSender() {
//        return new DefaultSmsCodeSender();
//    }

}

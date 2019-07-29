package com.hk.core.autoconfigure.authentication.security.oauth2;

import com.hk.core.autoconfigure.authentication.security.AuthenticationProperties;
import com.hk.core.autoconfigure.authentication.security.SecurityAuthenticationAutoConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;

import com.hk.core.autoconfigure.exception.Oauth2ErrorController;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.Filter;
import java.util.List;

/**
 * @author huangkai
 * @date 2018-12-27 15:57
 */
@Configuration
@AutoConfigureAfter(SecurityAuthenticationAutoConfiguration.class)
@ConditionalOnClass(value = {OAuth2ClientAuthenticationProcessingFilter.class})
public class Oauth2AutoConfiguration {

    private final AuthenticationProperties authenticationProperties;

    public Oauth2AutoConfiguration(AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    /**
     * 客户端登陆Oauth2 认证出错时的处理器
     */
    @Bean
    public Oauth2ErrorController oauth2ErrorController() {
        return new Oauth2ErrorController();
    }

    /**
     * spring bean 后置处理器
     *
     * 通过源码分析 ，没有找到怎么配置 {@link OAuth2ClientAuthenticationProcessingFilter} 其它属性值
     *
     * @see org.springframework.boot.autoconfigure.security.oauth2.client.SsoSecurityConfigurer.OAuth2ClientAuthenticationConfigurer
     */
    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof FilterChainProxy) {
                    List<SecurityFilterChain> filterChains = ((FilterChainProxy) bean).getFilterChains();
                    for (SecurityFilterChain filterChain : filterChains) {
                        for (Filter filter : filterChain.getFilters()) {
                            if (filter instanceof OAuth2ClientAuthenticationProcessingFilter) {
                                OAuth2ClientAuthenticationProcessingFilter procssingFilter = (OAuth2ClientAuthenticationProcessingFilter) filter;
                                SimpleUrlAuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();
                                authenticationFailureHandler.setAllowSessionCreation(authenticationProperties.isAllowSessionCreation());
                                authenticationFailureHandler.setDefaultFailureUrl(authenticationProperties.getDefaultFailureUrl());
                                authenticationFailureHandler.setUseForward(authenticationProperties.isForwardToDestination());
                                procssingFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
                            }
                        }
                    }
                }
                return bean;
            }
        };
    }

}

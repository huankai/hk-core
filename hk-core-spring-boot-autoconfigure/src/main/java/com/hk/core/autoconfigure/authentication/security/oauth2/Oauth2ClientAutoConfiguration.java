package com.hk.core.autoconfigure.authentication.security.oauth2;

import com.hk.core.authentication.oauth2.authentication.CodeAuthenticationSuccessHandler;
import com.hk.core.authentication.oauth2.provider.token.RequestIpJwtTokenStore;
import com.hk.core.authentication.oauth2.session.HashMapBackedSessionMappingStorage;
import com.hk.core.authentication.oauth2.session.SessionMappingStorage;
import com.hk.core.authentication.oauth2.session.SingleSignOutHttpSessionListener;
import com.hk.core.autoconfigure.authentication.AuthenticationProperties;
import com.hk.core.autoconfigure.authentication.security.SecurityAuthenticationAutoConfiguration;
import com.hk.core.autoconfigure.exception.Oauth2ErrorController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * oauth2 client 自动 配置
 *
 * @author huangkai
 * @date 2018-12-27 15:57
 */
@Configuration
@AutoConfigureAfter(SecurityAuthenticationAutoConfiguration.class)
@ConditionalOnBean(OAuth2ClientContext.class)
public class Oauth2ClientAutoConfiguration {

    private final AuthenticationProperties authenticationProperties;

    public Oauth2ClientAutoConfiguration(AuthenticationProperties authenticationProperties) {
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
     * @see org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration.JwtTokenServicesConfiguration#jwtTokenStore()
     */
    @Bean
    public JwtTokenStore jwtTokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new RequestIpJwtTokenStore(jwtAccessTokenConverter);
    }

    /**
     * 自定义 {@link OAuth2RestTemplate}
     *
     * @return {@link UserInfoRestTemplateCustomizer}
     */
    @Bean
    public UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer() {
        return template -> {
//            AuthenticationProperties.LoginProperties loginProperties = authenticationProperties.getLogin();
//            LogoutAuthorizationCodeAccessTokenProvider authorizationCodeAccessTokenProvider = new LogoutAuthorizationCodeAccessTokenProvider();
//            authorizationCodeAccessTokenProvider.setLogoutUrl(loginProperties.getLogoutUrl());
//            authorizationCodeAccessTokenProvider.setForceHttps(authenticationProperties.getOauth2().isForceHttps());
            AuthorizationCodeAccessTokenProvider accessTokenProvider = new AuthorizationCodeAccessTokenProvider();
            accessTokenProvider.setStateMandatory(false);
            template.setAccessTokenProvider(accessTokenProvider);//简化，只需要认证码模式

        };
    }

    /**
     * spring bean 后置处理器
     * <p>
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
                    var filterChains = ((FilterChainProxy) bean).getFilterChains();
                    for (var filterChain : filterChains) {
                        for (var filter : filterChain.getFilters()) {
                            if (filter instanceof OAuth2ClientAuthenticationProcessingFilter) {
                                OAuth2ClientAuthenticationProcessingFilter processingFilter = (OAuth2ClientAuthenticationProcessingFilter) filter;
                                processingFilter.setAuthenticationSuccessHandler(new CodeAuthenticationSuccessHandler());
                                SimpleUrlAuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();
                                authenticationFailureHandler.setAllowSessionCreation(authenticationProperties.isAllowSessionCreation());
                                authenticationFailureHandler.setDefaultFailureUrl(authenticationProperties.getOauth2().getOauth2FailureUrl());
                                authenticationFailureHandler.setUseForward(authenticationProperties.isForwardToDestination());
                                processingFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
                            }
                        }
                    }
                }
                return bean;
            }
        };
    }

    /**
     * oauth2 client 单点退出配置
     */
    @Configuration
    @ConditionalOnClass(SessionMappingStorage.class)
    static class OAuth2ClientSingleSignOutAuthenticationConfiguration {

        @Bean
        @ConditionalOnMissingBean(value = {SessionMappingStorage.class})
        public SessionMappingStorage sessionMappingStorage() {
            return new HashMapBackedSessionMappingStorage();
        }

        /**
         * oauth2 单点退出 session 监听器，必要配置，确保 {@link SessionMappingStorage} 中过期的 Session 自动清除
         */
        @Bean
        public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener(SessionMappingStorage sessionMappingStorage) {
            ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> registrationBean = new ServletListenerRegistrationBean<>();
            registrationBean.setListener(new SingleSignOutHttpSessionListener(sessionMappingStorage));
            registrationBean.setOrder(0);
            return registrationBean;
        }

    }


}

package com.hk.core.autoconfigure.authentication.security.oauth2;

import com.hk.core.authentication.oauth2.client.token.grant.code.LogoutAuthorizationCodeAccessTokenProvider;
import com.hk.core.authentication.oauth2.session.HashMapBackedSessionMappingStorage;
import com.hk.core.authentication.oauth2.session.SessionMappingStorage;
import com.hk.core.authentication.oauth2.session.SingleSignOutHttpSessionListener;
import com.hk.core.autoconfigure.authentication.security.AuthenticationProperties;
import com.hk.core.autoconfigure.authentication.security.SecurityAuthenticationAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;

import java.util.Arrays;

/**
 * oauth2 client 单点退出配置
 *
 * @author kevin
 * @date 2019-4-27 18:03
 */
@Configuration
@ConditionalOnClass(SessionMappingStorage.class)
@ConditionalOnBean(OAuth2ClientContext.class)
@AutoConfigureAfter(SecurityAuthenticationAutoConfiguration.class)
public class OAuth2SingleSignOutAuthenticationConfiguration {

    @Autowired
    private AuthenticationProperties properties;

    /**
     * HashMap 存储 access_token 与 客户端 Session
     */
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

    /**
     * 自定义 {@link OAuth2RestTemplate}
     *
     * @return {@link UserInfoRestTemplateCustomizer}
     */
    @Bean
    public UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer() {
        return template -> {
            AuthenticationProperties.LoginProperties loginProperties = properties.getLogin();
            LogoutAuthorizationCodeAccessTokenProvider authorizationCodeAccessTokenProvider = new LogoutAuthorizationCodeAccessTokenProvider();
            authorizationCodeAccessTokenProvider.setLogoutUrl(loginProperties.getLogoutUrl());
            authorizationCodeAccessTokenProvider.setForceHttps(loginProperties.isForceHttps());
            template.setAccessTokenProvider(new AccessTokenProviderChain(Arrays.<AccessTokenProvider>asList(
                    authorizationCodeAccessTokenProvider, new ImplicitAccessTokenProvider(),
                    new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider())));
        };
    }

}

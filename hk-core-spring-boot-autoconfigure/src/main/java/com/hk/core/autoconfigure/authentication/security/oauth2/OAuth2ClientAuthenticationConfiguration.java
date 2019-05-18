package com.hk.core.autoconfigure.authentication.security.oauth2;

import com.hk.core.authentication.oauth2.session.HashMapBackedSessionMappingStorage;
import com.hk.core.authentication.oauth2.session.SessionMappingStorage;
import com.hk.core.authentication.oauth2.session.SingleSignOutHttpSessionListener;
import com.hk.core.web.Webs;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import javax.servlet.http.HttpSession;

/**
 * @author kevin
 * @date 2019-4-27 18:03
 */
@Configuration
@ConditionalOnClass(SessionMappingStorage.class)
@ConditionalOnBean(OAuth2ClientContext.class)
public class OAuth2ClientAuthenticationConfiguration {

    /**
     * Session 存储
     */
    @Bean
    @ConditionalOnMissingBean(value = {SessionMappingStorage.class})
    public SessionMappingStorage sessionMappingStorage() {
        return new HashMapBackedSessionMappingStorage();
    }

    /**
     * 退出 session 监听器
     */
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener(SessionMappingStorage sessionMappingStorage) {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> registrationBean = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new SingleSignOutHttpSessionListener(sessionMappingStorage));
        return registrationBean;
    }

    /**
     * 自定认 {@link OAuth2RestTemplate}
     *
     * @return {@link UserInfoRestTemplateCustomizer}
     */
//    @Bean
//    public UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer() {
//        return new UserInfoRestTemplateCustomizer() {
//            @Override
//            public void customize(OAuth2RestTemplate template) {
//                template.setRequestFactory(new SimpleClientHttpRequestFactory());
//            }
//        };
//    }

    /**
     * 认证监听器，当认证成功后会回调
     *
     * @param sessionMappingStorage
     * @return
     * @see org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter#eventPublisher
     */
    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> authenticationSuccessEvent(SessionMappingStorage sessionMappingStorage) {
        return event -> {
            Authentication authentication = event.getAuthentication();
            if (authentication instanceof OAuth2Authentication) {
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
                HttpSession session = Webs.getHttpServletRequest().getSession(false);
                if (null != session) {
                    sessionMappingStorage.addSessionById(details.getTokenValue(), session);
                }
            }
        };

    }
}

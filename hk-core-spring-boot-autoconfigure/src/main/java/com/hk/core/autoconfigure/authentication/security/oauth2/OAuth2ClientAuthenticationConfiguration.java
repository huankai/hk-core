package com.hk.core.autoconfigure.authentication.security.oauth2;

import com.hk.core.authentication.oauth2.session.HashMapBackedSessionMappingStorage;
import com.hk.core.authentication.oauth2.session.SessionMappingStorage;
import com.hk.core.authentication.oauth2.session.SingleSignOutHttpSessionListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

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
    @EventListener
    public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener(SessionMappingStorage sessionMappingStorage) {
        return new SingleSignOutHttpSessionListener(sessionMappingStorage);
    }

    /**
     * 自定认 {@link OAuth2RestTemplate}
     *
     * @return {@link UserInfoRestTemplateCustomizer}
     */
    @Bean
    public UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer() {
        return new UserInfoRestTemplateCustomizer() {
            @Override
            public void customize(OAuth2RestTemplate template) {
                template.setRequestFactory(new SimpleClientHttpRequestFactory());
            }
        };
    }

//    /**
//     * 认证监听器，当认证成功后会回调
//     *
//     * @param sessionMappingStorage
//     * @return
//     * @see org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter#eventPublisher
//     */
//    @Bean
//    public ApplicationListener<AuthenticationSuccessEvent> authenticationSuccessEvent(SessionMappingStorage sessionMappingStorage) {
//        return event -> {
//            Authentication authentication = event.getAuthentication();
//            if (authentication instanceof OAuth2Authentication) {
//                UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
//                // TODO 这里有个问题，就是用户每个用户只能在一个地方登陆
//                sessionMappingStorage.addSessionById(principal.getUserId(),
//                        Webs.getHttpServletRequest().getSession(false));
//            }
//        };
//
//    }
}

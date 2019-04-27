package com.hk.core.autoconfigure.authentication.security.oauth2;

import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.oauth2.session.HashMapBackedSessionMappingStorage;
import com.hk.core.authentication.oauth2.session.SessionMappingStorage;
import com.hk.core.web.Webs;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

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
     * 认证监听器，当认证成功后会回调
     *
     * @param sessionMappingStorage
     * @return
     * @see org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#eventPublisher
     */
    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> authenticationSuccessEvent(SessionMappingStorage sessionMappingStorage) {
        return event -> {
            Authentication authentication = event.getAuthentication();
            if (authentication instanceof OAuth2Authentication) {
                UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
                // TODO 这里有个问题，就是用户每个用户只能在一个地方登陆
                sessionMappingStorage.addSessionById(principal.getUserId(),
                        Webs.getHttpServletRequest().getSession(false));
            }
        };

    }
}

package com.hk.core.autoconfigure.authentication.security.integration;

import com.hk.core.authentication.api.SecurityContextUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.integration.security.channel.ChannelSecurityInterceptor;
import org.springframework.integration.security.channel.SecurityContextPropagationChannelInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * @author huangkai
 * @date 2019-07-27 22:29
 */
@Configuration
@EnableIntegration
@ConditionalOnClass(SecurityContextPropagationChannelInterceptor.class)
public class IntegrationSecurityAutoConfiguration {

    @Bean
    @GlobalChannelInterceptor
    public ChannelInterceptor securityContextPropagationInterceptor() {
        return new SecurityContextPropagationChannelInterceptor() {
            @Override
            protected Authentication obtainPropagatingContext(Message<?> message, MessageChannel channel) {
                if (SecurityContextUtils.isAuthenticated()) {
                    return SecurityContextHolder.getContext().getAuthentication();
                }
                return null;
            }
        };
    }

//    @Bean
//    public ChannelSecurityInterceptor channelSecurityInterceptor() {
//        ChannelSecurityInterceptor channelSecurityInterceptor = new ChannelSecurityInterceptor();
//        channelSecurityInterceptor.setAuthenticationManager(new AuthenticationManager() {
//            @Override
//            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//                    return authentication;
//            }
//        });
//        channelSecurityInterceptor.setAccessDecisionManager(new AccessDecisionManager() {
//            @Override
//            public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
//                System.out.println("000");
//            }
//
//            @Override
//            public boolean supports(ConfigAttribute attribute) {
//                return true;
//            }
//
//            @Override
//            public boolean supports(Class<?> clazz) {
//                return true;
//            }
//        });
//        return channelSecurityInterceptor;
//
//    }
}

package com.hk.core.autoconfigure.authentication.security.integration;

import com.hk.authentication.interceptors.AuthenticationChannelInterceptor;
import com.hk.authentication.rabbit.advice.RabbitSecurityContextMethodAdvice;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptor;

/**
 * spring cloud stream 配置
 *
 * @author huangkai
 * @date 2019-07-27 22:29
 */
@Configuration
@ConditionalOnClass(value = {AuthenticationChannelInterceptor.class})
public class IntegrationSecurityAutoConfiguration {

    /**
     * 全局拦截器
     */
    @Bean
    @GlobalChannelInterceptor
    public ChannelInterceptor securityContextPropagationInterceptor() {
        return new AuthenticationChannelInterceptor();
    }

    /**
     * rabbit 认证
     */
    @Configuration
    @ConditionalOnClass(value = {AbstractMessageListenerContainer.class})
    static class RabbitIntegrationSecurityAutoConfiguration {

        @Bean
        public ListenerContainerCustomizer<AbstractMessageListenerContainer> containerCustomizer() {
            return (container, dest, group) -> container.setAdviceChain(new RabbitSecurityContextMethodAdvice());
        }
    }
}

package com.hk.core.autoconfigure.authentication.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.security.messaging.web.csrf.CsrfChannelInterceptor;


/**
 * spring security websocket 配置类
 *
 * @author kevin
 */
@Configuration
@ConditionalOnClass(value = {SecurityContextChannelInterceptor.class})
public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    /**
     * 配置 {@link ChannelRegistration}，可以添加自定义拦截器
     *
     * @param registration registration
     */
    @Override
    protected void customizeClientInboundChannel(ChannelRegistration registration) {

    }

    /**
     * 配置消息的权限，这里配置任意消息都需要认证，包括建立连接时
     *
     * @param messages messages
     */
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.anyMessage().authenticated();
//        messages.simpTypeMatchers(SimpMessageType.CONNECT).permitAll(); //连接时不需要认证
//        messages.simpDestMatchers("/user/**").permitAll(); // 发送到 /user/** 的不需要认证
    }

    /**
     * 是否支持 csrf, 默认为 false
     * 如果配置为 false ：会添加 {@link CsrfChannelInterceptor} 拦截器
     *
     * @return true
     * @see AbstractSecurityWebSocketMessageBrokerConfigurer#configureClientInboundChannel(ChannelRegistration)
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}

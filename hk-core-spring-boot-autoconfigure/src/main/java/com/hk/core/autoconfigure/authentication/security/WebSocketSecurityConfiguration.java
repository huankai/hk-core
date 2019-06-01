package com.hk.core.autoconfigure.authentication.security;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.security.messaging.web.csrf.CsrfChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.server.HandshakeHandler;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.SecurityContextUtils;
import com.hk.core.autoconfigure.message.websocket.WebSocketProperties;
import com.hk.message.websocket.WebsocketMessager;

/**
 * <pre>
 * spring security websocket 配置类，
 * 如果不与 spring security 整合，可以直接使用 {@link com.hk.core.autoconfigure.message.websocket.WebSocketAutoConfiguration}  配置类，如果使用这个配置，则不能通过
 * {@link SecurityContextUtils#getPrincipal()} 获取用户信息
 * </pre>
 *
 * @author kevin
 */
@Configuration
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(WebSocketProperties.class)
@ConditionalOnClass(value = {SecurityContextChannelInterceptor.class})
public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    private final WebSocketProperties webSocketProperties;

    private WebSocketHandlerDecoratorFactory webSocketHandlerDecoratorFactory;

//	/**
//	 * channel 拦截器
//	 */
//	private List<ChannelInterceptor> channelInterceptors;

    /**
     * 握手回调
     */
    private HandshakeHandler handshakeHandler;

    public WebSocketSecurityConfiguration(WebSocketProperties webSocketProperties,
                                          ObjectProvider<WebSocketHandlerDecoratorFactory> decoratorFactories,
//			ObjectProvider<List<ChannelInterceptor>> channelInterceptors,
                                          ObjectProvider<HandshakeHandler> handshakeHandler) {
        this.webSocketProperties = webSocketProperties;
        this.webSocketHandlerDecoratorFactory = decoratorFactories.getIfAvailable();
//		this.channelInterceptors = channelInterceptors.getIfAvailable();
        this.handshakeHandler = handshakeHandler.getIfAvailable();
    }

    @Bean("websocketMessager")
    public WebsocketMessager websocketMessager(SimpMessagingTemplate messagingTemplate) {
        return new WebsocketMessager(messagingTemplate);
    }

//	/**
//	 * 配置 {@link ChannelRegistration}，可以添加自定义拦截器
//	 *
//	 * @param registration registration
//	 */
//	@Override
//	protected void customizeClientInboundChannel(ChannelRegistration registration) {
//		if (CollectionUtils.isNotEmpty(channelInterceptors)) {
//			registration.interceptors(channelInterceptors.toArray(new ChannelInterceptor[] {}));
//		}
//	}

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(webSocketProperties.getEndpoint()).setHandshakeHandler(handshakeHandler)
                .setAllowedOrigins(webSocketProperties.getAllowedOrigins()).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        DefaultManagedTaskScheduler scheduler = new DefaultManagedTaskScheduler();
        registry.enableSimpleBroker(webSocketProperties.getSimpleBrokers()).setTaskScheduler(scheduler);
        String[] applicationDestinationPrefixes = webSocketProperties.getApplicationDestinationPrefixes();
        if (ArrayUtils.isNotEmpty(applicationDestinationPrefixes)) {
            registry.setApplicationDestinationPrefixes(applicationDestinationPrefixes);
        }
        String userDestinationPrefix = webSocketProperties.getUserDestinationPrefix();
        if (StringUtils.isNotEmpty(userDestinationPrefix)) {
            registry.setUserDestinationPrefix(userDestinationPrefix);
        }
        registry.setCacheLimit(webSocketProperties.getCacheLimit());
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
     * 是否支持 csrf, 默认为 false 如果配置为 false ：会添加 {@link CsrfChannelInterceptor} 拦截器
     *
     * @return true
     * @see AbstractSecurityWebSocketMessageBrokerConfigurer#configureClientInboundChannel(ChannelRegistration)
     */
    @Override
    protected boolean sameOriginDisabled() {
        return webSocketProperties.isOriginDisabled();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        if (this.webSocketHandlerDecoratorFactory != null) {
            registration.addDecoratorFactory(webSocketHandlerDecoratorFactory);
        }
    }
}

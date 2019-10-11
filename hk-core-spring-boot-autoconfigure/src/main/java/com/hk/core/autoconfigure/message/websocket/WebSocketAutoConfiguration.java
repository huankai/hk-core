package com.hk.core.autoconfigure.message.websocket;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.message.websocket.WebsocketMessager;
import com.hk.message.websocket.handlers.CheckLoginHandshakeInterceptor;
import com.hk.message.websocket.publish.WebSocketMessagePublish;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin
 * @date 2018-09-21 13:14
 */
@Configuration
@ConditionalOnClass(WebSocketMessagePublish.class)
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketAutoConfiguration implements WebSocketMessageBrokerConfigurer {

    private WebSocketProperties webSocketProperties;

    /**
     * 握手回调
     */
    private HandshakeHandler handshakeHandler;

    /**
     * websocket 握手、连接、断开、处理器
     *
     * @see hk-examples 项目中的 hk-message-websocket-example 配置类： WebSocketConfiguration#configureWebSocketTransport
     */
    private WebSocketHandlerDecoratorFactory webSocketHandlerDecoratorFactory;

    /**
     * Channel 拦截器，可以处理消息发送前，消息发送后执行一些操作
     */
    private List<ChannelInterceptor> channelInterceptors;

    public WebSocketAutoConfiguration(WebSocketProperties webSocketProperties,
                                      ObjectProvider<WebSocketHandlerDecoratorFactory> decoratorFactories,
                                      ObjectProvider<List<ChannelInterceptor>> channelInterceptors,
                                      ObjectProvider<HandshakeHandler> handshakeHandlers) {
        this.webSocketProperties = webSocketProperties;
        this.webSocketHandlerDecoratorFactory = decoratorFactories.getIfAvailable();
        this.channelInterceptors = channelInterceptors.getIfAvailable();
        this.handshakeHandler = handshakeHandlers.getIfAvailable();
    }

    @Bean("websocketMessager")
    public WebsocketMessager websocketMessager(SimpMessagingTemplate messagingTemplate) {
        return new WebsocketMessager(messagingTemplate);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        List<HandshakeInterceptor> interceptorList = new ArrayList<>();
        if (webSocketProperties.isNeedLogin()) {
            interceptorList.add(new CheckLoginHandshakeInterceptor());
        }
        HandshakeInterceptor[] interceptors = interceptorList.toArray(new HandshakeInterceptor[]{});

        registry.addEndpoint(webSocketProperties.getEndpoint())
                .addInterceptors(interceptors)
//                .setHandshakeHandler(new PrincipalHandshakeHandler())
                .setHandshakeHandler(handshakeHandler)
                .setAllowedOrigins(webSocketProperties.getAllowedOrigins())
                .withSockJS()
                .setInterceptors(interceptors);

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        if (CollectionUtils.isNotEmpty(channelInterceptors)) {
            registry.configureBrokerChannel().interceptors(channelInterceptors.toArray(new ChannelInterceptor[]{}));
        }
        DefaultManagedTaskScheduler taskScheduler = new DefaultManagedTaskScheduler();
        registry.enableSimpleBroker(webSocketProperties.getSimpleBrokers())
//                .setHeartbeatValue(new long[]{5000, 5000}) //设置与客户端心跳机制
                .setTaskScheduler(taskScheduler);

        String[] applicationDestinationPrefixes = webSocketProperties.getApplicationDestinationPrefixes();
        if (ArrayUtils.isNotEmpty(applicationDestinationPrefixes)) {
            registry.setApplicationDestinationPrefixes(applicationDestinationPrefixes);
        }
        String userDestinationPrefix = webSocketProperties.getUserDestinationPrefix();
        if (StringUtils.isNotEmpty(userDestinationPrefix)) {
            registry.setUserDestinationPrefix(userDestinationPrefix);
        }
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        if (this.webSocketHandlerDecoratorFactory != null) {
            registry.addDecoratorFactory(webSocketHandlerDecoratorFactory);
        }
    }


}

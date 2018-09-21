package com.hk.core.autoconfigure.websocket;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.message.websocket.publish.WebSocketMessagePublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;

/**
 * @author: sjq-278
 * @date: 2018-09-21 13:14
 */
@Configuration
@ConditionalOnClass(WebSocketMessage.class)
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    /**
     * 拦截器
     */
    @Autowired(required = false)
    private List<HandshakeInterceptor> interceptors;

    @Autowired(required = false)
    private HandshakeHandler handshakeHandler;

    private WebSocketProperties webSocketProperties;

    public WebSocketConfiguration(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        HandshakeInterceptor[] interceptors = null;
        if (CollectionUtils.isNotEmpty(this.interceptors)) {
            interceptors = this.interceptors.toArray(new HandshakeInterceptor[]{});
        }
        registry.addEndpoint(webSocketProperties.getEndpoint())
                .addInterceptors(interceptors)
                .setHandshakeHandler(handshakeHandler)
                .setAllowedOrigins(webSocketProperties.getAllowedOrigins())
                .withSockJS()
                .setInterceptors(interceptors);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(webSocketProperties.getSimpleBrokers());
        String[] applicationDestinationPrefixes = webSocketProperties.getApplicationDestinationPrefixes();
        if (ArrayUtils.isNotEmpty(applicationDestinationPrefixes)) {
            registry.setApplicationDestinationPrefixes(applicationDestinationPrefixes);
        }
        String userDestinationPrefix = webSocketProperties.getUserDestinationPrefix();
        if (StringUtils.isNotEmpty(userDestinationPrefix)) {
            registry.setUserDestinationPrefix(userDestinationPrefix);
        }
    }

    @Configuration
    @ConditionalOnClass(WebSocketMessagePublish.class)
    protected class WebSocketMessagePublishConfiguration {

        @Bean("webSocketMessagePublish")
        public WebSocketMessagePublish webSocketMessagePublish(SimpMessagingTemplate messagingTemplate) {
            return new WebSocketMessagePublish(messagingTemplate);
        }
    }


}

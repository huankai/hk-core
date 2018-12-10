package com.hk.core.autoconfigure.message.websocket;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.StringUtils;
import com.hk.message.websocket.WebsocketMessager;
import com.hk.message.websocket.handlers.CheckLoginHandshakeInterceptor;
import com.hk.message.websocket.handlers.PrincipalHandshakeHandler;
import com.hk.message.websocket.publish.WebSocketMessagePublish;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: kevin
 * @date: 2018-09-21 13:14
 */
@Configuration
@ConditionalOnClass(WebSocketMessagePublish.class)
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketAutoConfiguration implements WebSocketMessageBrokerConfigurer {

    private WebSocketProperties webSocketProperties;

    public WebSocketAutoConfiguration(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
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
                .setHandshakeHandler(new PrincipalHandshakeHandler())
                .setAllowedOrigins(webSocketProperties.getAllowedOrigins())
                .withSockJS()
                .setInterceptors(interceptors);

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
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


}

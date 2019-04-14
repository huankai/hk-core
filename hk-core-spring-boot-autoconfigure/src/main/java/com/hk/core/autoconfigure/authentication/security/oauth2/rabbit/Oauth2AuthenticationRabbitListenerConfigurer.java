package com.hk.core.autoconfigure.authentication.security.oauth2.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author huangkai
 * @date 2019-04-14 16:02
 */
@RequiredArgsConstructor
public class Oauth2AuthenticationRabbitListenerConfigurer implements RabbitListenerConfigurer {

    private final ConnectionFactory connectionFactory;

    private final ResourceServerTokenServices defaultTokenServices;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        Oauth2MessageListenerContainerFactory messageListenerContainerFactory = new Oauth2MessageListenerContainerFactory(defaultTokenServices);
        messageListenerContainerFactory.setConnectionFactory(connectionFactory);
        registrar.setContainerFactory(messageListenerContainerFactory);
    }
}

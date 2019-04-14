package com.hk.core.autoconfigure.authentication.security.oauth2.rabbit;

import com.hk.stream.authentication.security.oauth2.Oauth2AuthenticationMessageListenerContainer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author huangkai
 * @date 2019-04-14 16:03
 */
@RequiredArgsConstructor
public class Oauth2MessageListenerContainerFactory extends SimpleRabbitListenerContainerFactory {

    private final ResourceServerTokenServices tokenServices;

    @Override
    protected SimpleMessageListenerContainer createContainerInstance() {
        return new Oauth2AuthenticationMessageListenerContainer(tokenServices);
    }
}

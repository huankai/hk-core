package com.hk.stream.authentication.security.oauth2;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Map;
import java.util.Objects;

/**
 * @author huangkai
 * @date 2019-04-14 22:04
 * @see https://stackoverflow.com/questions/50358936/oauth2-authorization-with-spring-security-and-rabbitmq
 */
@RequiredArgsConstructor
public class Oauth2AuthenticationMessageListenerContainer extends SimpleMessageListenerContainer {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final ResourceServerTokenServices tokenServices;

    @Override
    protected void executeListener(Channel channel, Message messageIn) {
        MessageProperties messageProperties = messageIn.getMessageProperties();
        Map<String, Object> headers = messageProperties.getHeaders();
        Object authorization = headers.get(AUTHORIZATION_HEADER);
        if (Objects.nonNull(authorization)) {
            try {
                OAuth2Authentication authentication = tokenServices.loadAuthentication(authorization.toString());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (OAuth2Exception | AuthenticationException e) {
                logger.warn("获取认证信息失败:" + authorization, e);
            }
        }
        super.executeListener(channel, messageIn);
    }

}

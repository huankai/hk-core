package com.hk.authentication.rabbit.listener;

import com.hk.authentication.headers.Header;
import com.hk.commons.util.JsonUtils;
import com.hk.core.authentication.api.UserPrincipal;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Objects;

/**
 * 从请求头中获取认证信息
 *
 * @author huangkai
 * @date 2019-4-15 11:27
 * @see com.hk.authentication.interceptors.AuthenticationChannelInterceptor
 */
public class SimpleSecurityContextMessageListenerContainer extends SimpleMessageListenerContainer {

    public SimpleSecurityContextMessageListenerContainer(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * <pre>
     * 消息消费之前会执行，从请求头中获取用户信息，并设置到spring security context 中
     * 对于 oauth2 认证，为什么不是将 access_token 添加到　请求头中来获取用户信息，原因是：
     *  access_token 有过期时间，如果此消息在过期时间内都没有消费，则无法获取用户信息
     *
     *  使用 oauth2 获取用户信息 可查看 : https://stackoverflow.com/questions/50358936/oauth2-authorization-with-spring-security-and-rabbitmq
     *
     * </pre>
     *
     * @param channel   channel
     * @param messageIn messageIn
     */
    @Override
    protected void executeListener(Channel channel, Message messageIn) {
        MessageProperties messageProperties = messageIn.getMessageProperties();
        Map<String, Object> headers = messageProperties.getHeaders();
        Object authorization = headers.get(Header.AUTHORIZATION_HEADER);
        if (Objects.nonNull(authorization)) {
            UserPrincipal userPrincipal = JsonUtils.deserialize(authorization.toString(), UserPrincipal.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        super.executeListener(channel, messageIn);
    }

    /*
     * oauth 2 实现
     */
//    private final ResourceServerTokenServices tokenServices;

//    @Override
//    protected void executeListener(Channel channel, Message messageIn) {
//        MessageProperties messageProperties = messageIn.getMessageProperties();
//        Map<String, Object> headers = messageProperties.getHeaders();
//        Object authorization = headers.get(AUTHORIZATION_HEADER);
//        if (Objects.nonNull(authorization)) {
//            try {
//                OAuth2Authentication authentication = tokenServices.loadAuthentication(authorization.toString());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } catch (OAuth2Exception | AuthenticationException e) {
//                logger.warn("获取认证信息失败:" + authorization, e);
//            }
//        }
//        super.executeListener(channel, messageIn);
//    }
}



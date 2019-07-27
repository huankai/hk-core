package com.hk.authentication.interceptors;

import com.hk.authentication.headers.Header;
import com.hk.commons.util.JsonUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.SecurityContextUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

/**
 * 认证头拦截器，将当前用户信息以请求头方式发送给消息服务器，消费者可以通过请求头信息获取用户信息
 *
 * @author huangkai
 * @date 2019-4-15 11:25
 * @see AuthenticationMessageListenerContainer#executeListener(Channel, org.springframework.amqp.core.Message)
 */
public class AuthenticationChannelInterceptor implements ChannelInterceptor {

    /**
     * 消费发送前调用
     *
     * @param message message
     * @param channel channel
     * @return {@link Message}
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        MessageHeaders headers = message.getHeaders();
        String authentication = (String) headers.get(Header.AUTHORIZATION_HEADER);
        if (StringUtils.isEmpty(authentication) && SecurityContextUtils.isAuthenticated()) {
            return MessageBuilder.fromMessage(message)
                    .setHeader(Header.AUTHORIZATION_HEADER,
                            JsonUtils.serialize(SecurityContextUtils.getPrincipal())).build();
        }
        return message;
    }
}

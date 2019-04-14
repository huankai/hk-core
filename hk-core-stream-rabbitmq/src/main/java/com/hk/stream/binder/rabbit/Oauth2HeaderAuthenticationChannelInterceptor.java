package com.hk.stream.binder.rabbit;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

/**
 * @author huangkai
 * @date 2019-04-14 22:56
 */
public class Oauth2HeaderAuthenticationChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // todo 未完成
//        MessageHeaders headers = message.getHeaders();
//        String authen = (String) headers.get(Oauth2AuthenticationMessageListenerContainer.AUTHORIZATION_HEADER);
//        if (SecurityContextUtils.isAuthenticated() && StringUtils.isEmpty(authen)) {
//            headers.put(Oauth2AuthenticationMessageListenerContainer.AUTHORIZATION_HEADER, SecurityContextUtils.getPrincipal().getUserId());
//        }
        return message;
    }
}

package com.hk.stream.messaging;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.SecurityContextUtils;
import com.hk.stream.authentication.security.oauth2.Oauth2AuthenticationMessageListenerContainer;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;
import java.util.UUID;

/**
 * @author huangkai
 * @date 2019-04-14 23:24
 */
public class Oauth2MessageHeaders extends MessageHeaders {

    public Oauth2MessageHeaders(Map<String, Object> headers) {
        this(headers, null, null);
    }

    protected Oauth2MessageHeaders(Map<String, Object> headers, UUID id, Long timestamp) {
        super(headers, id, timestamp);

        // todo 未完成
//        if (SecurityContextUtils.isAuthenticated()) {
//            String value = (String) headers.get(Oauth2AuthenticationMessageListenerContainer.AUTHORIZATION_HEADER);
//            if (StringUtils.isEmpty(value)) {
//                headers.put(Oauth2AuthenticationMessageListenerContainer.AUTHORIZATION_HEADER, SecurityContextUtils.getPrincipal().getUserId());
//            }
//        }
    }
}

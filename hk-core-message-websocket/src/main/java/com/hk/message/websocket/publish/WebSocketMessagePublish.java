package com.hk.message.websocket.publish;

import com.hk.commons.util.ObjectUtils;
import com.hk.commons.util.StringUtils;
import com.hk.message.api.publish.MessagePublish;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Objects;

/**
 * @author: sjq-278
 * @date: 2018-09-21 08:42
 */
public class WebSocketMessagePublish implements MessagePublish {

    private SimpMessagingTemplate messagingTemplate;

    public WebSocketMessagePublish(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void send(String to, Object destination, Object payload) {
        String dest = Objects.requireNonNull(ObjectUtils.toString(destination));
        if (StringUtils.isEmpty(to)) {
            messagingTemplate.convertAndSend(dest, payload);
        } else {
            messagingTemplate.convertAndSendToUser(to, dest, payload);
        }
    }
}

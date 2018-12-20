package com.hk.message.websocket;

import com.hk.message.api.Message;
import com.hk.message.api.Messager;
import com.hk.message.api.publish.MessagePublish;
import com.hk.message.websocket.publish.WebSocketMessagePublish;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * example:
 * <pre>
 *     messager
 *     .publish(xxx)
 *     .to(xx)
 *     .send();
 * </pre>
 *
 * @author huangkai
 */
public class WebsocketMessager implements Messager {

    private SimpMessagingTemplate messagingTemplate;

    public WebsocketMessager(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public MessagePublish publish(Message message) {
        return new WebSocketMessagePublish(messagingTemplate, message);
    }
}

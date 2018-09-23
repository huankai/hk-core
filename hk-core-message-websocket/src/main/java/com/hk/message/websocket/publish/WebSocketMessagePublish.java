package com.hk.message.websocket.publish;

import com.hk.message.api.*;
import com.hk.message.api.publish.MessagePublish;
import com.hk.message.api.subject.MessageSubject;
import com.hk.message.api.subject.QueueMessageSubject;
import com.hk.message.api.subject.TopicMessageSubject;
import com.hk.message.api.subject.UserMessageSubject;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * @author: sjq-278
 * @date: 2018-09-21 08:42
 */
public class WebSocketMessagePublish implements MessagePublish {

    private SimpMessagingTemplate messagingTemplate;

    private Message message;

    private MessageSubject subject;

    public WebSocketMessagePublish(SimpMessagingTemplate messagingTemplate, Message message) {
        this.messagingTemplate = messagingTemplate;
        this.message = message;
    }

    @Override
    public MessagePublish to(MessageSubject subject) {
        this.subject = subject;
        return this;
    }

    @Override
    public void send() {
        if (UserMessageSubject.class.isAssignableFrom(subject.getClass())) {
            UserMessageSubject userMessageSubject = (UserMessageSubject) subject;
            userMessageSubject.getUserIdList()
                    .forEach(userId -> messagingTemplate.convertAndSendToUser(userId,
                            userMessageSubject.getQueueName(), message));
        } else if (TopicMessageSubject.class.equals(subject.getClass())) {
            messagingTemplate.convertAndSend(((TopicMessageSubject) subject).getTopic(), message);
        } else if (QueueMessageSubject.class.equals(subject.getClass())) {
            messagingTemplate.convertAndSend(((QueueMessageSubject) subject).getQueueName(), message);
        }
    }
}

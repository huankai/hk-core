package com.hk.message.api.publish;

import com.hk.message.api.subject.MessageSubject;

/**
 * @author: sjq-278
 * @date: 2018-09-21 08:37
 */
public interface MessagePublish {

    MessagePublish to(MessageSubject subject);

    void send();
}

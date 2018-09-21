package com.hk.message.api.publish;

/**
 * @author: sjq-278
 * @date: 2018-09-21 08:37
 */
public interface MessagePublish {

    void send(String to, Object destination, Object payload);
}

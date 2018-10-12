package com.hk.message.api;

import com.hk.message.api.publish.MessagePublish;

/**
 * @author: huangkai
 * @date: 2018-9-23 11:05
 */
public interface Messager {

    MessagePublish publish(Message message);
}

package com.hk.message.api.subject;

/**
 * @author: huangkai
 * @date: 2018-9-23 11:14
 */
public interface QueueMessageSubject extends MessageSubject {

    String getQueueName();
}

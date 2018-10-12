package com.hk.message.api.subject;

import lombok.Builder;
import lombok.Data;

/**
 * @author: huangkai
 * @date: 2018-9-23 11:39
 */
@Data
@Builder
public class SimpleQueueMessageSubject implements QueueMessageSubject {

    private String queueName;

    @Override
    public String getQueueName() {
        return queueName;
    }
}

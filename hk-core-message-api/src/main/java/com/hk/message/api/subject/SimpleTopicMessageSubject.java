package com.hk.message.api.subject;

import lombok.Builder;
import lombok.Data;

/**
 * @author: huangkai
 * @date: 2018-9-23 11:39
 */
@Data
@Builder
public class SimpleTopicMessageSubject implements TopicMessageSubject {

    private String topic;

    @Override
    public String getTopic() {
        return topic;
    }
}

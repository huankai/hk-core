package com.hk.message.api.subject;

import java.util.List;

/**
 * @author: huangkai
 * @date: 2018-9-23 11:20
 */
public interface UserMessageSubject extends QueueMessageSubject {

    List<String> getUserIdList();
}

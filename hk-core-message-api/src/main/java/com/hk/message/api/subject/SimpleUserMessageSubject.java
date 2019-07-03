package com.hk.message.api.subject;

import com.hk.commons.util.CollectionUtils;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangkai
 * @date 2018-9-23 11:39
 */
@Data
@Builder
public class SimpleUserMessageSubject implements UserMessageSubject {

    private List<Long> userList;

    private String queueName;

    public SimpleUserMessageSubject addUser(Long... users){
        if(null == userList){
            userList = new ArrayList<>();
        }
        CollectionUtils.addAllNotNull(userList,users);
        return this;
    }

    @Override
    public List<Long> getUserIdList() {
        return userList;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }
}

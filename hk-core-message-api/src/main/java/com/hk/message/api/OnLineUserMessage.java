package com.hk.message.api;

import lombok.Builder;
import lombok.Data;

/**
 * 在线用户数消息
 *
 * @author: huangkai
 * @date: 2018-9-23 11:47
 */
@Data
@Builder
@SuppressWarnings("serial")
public class OnLineUserMessage implements Message {

    private int onLineUser;
}

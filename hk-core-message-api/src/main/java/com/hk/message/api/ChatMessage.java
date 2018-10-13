package com.hk.message.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息
 *
 * @author: huangkai
 * @date: 2018-9-23 11:31
 */
@Data
@Builder
public class ChatMessage implements Message {

    private String formUser;

    private String toUser;

    private Object content;

    @Builder.Default
    private LocalDateTime sendTime = LocalDateTime.now();
}
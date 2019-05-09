package com.hk.oauth2.ticket;

/**
 * @author kevin
 * @date 2019-5-6 17:32
 */
public interface UniqueTicketIdGenerator {

    String getNewTicketId(String prefix);
}

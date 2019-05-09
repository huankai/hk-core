package com.hk.oauth2.ticket;

import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kevin
 * @date 2019-5-9 9:55
 */
public class ConcurrentTicketRegistry implements Oauth2ClientTicketRegistry {

    private final ConcurrentHashMap<String, List<OAuth2Request>> concurrentHashMap = new ConcurrentHashMap<>();

    @Override
    public void addClient(String ticketId, OAuth2Request oAuth2Request) {
        List<OAuth2Request> list = concurrentHashMap.getOrDefault(ticketId, new ArrayList<>());
        list.add(oAuth2Request);
        concurrentHashMap.put(ticketId, list);
    }

    @Override
    public List<OAuth2Request> getClients(String ticketId) {
        return concurrentHashMap.getOrDefault(ticketId, new ArrayList<>());
    }
}

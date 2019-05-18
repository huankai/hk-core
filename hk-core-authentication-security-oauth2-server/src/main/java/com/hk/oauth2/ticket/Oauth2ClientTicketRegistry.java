package com.hk.oauth2.ticket;

import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.List;

/**
 * @author kevin
 * @date 2019-5-9 9:52
 */
public interface Oauth2ClientTicketRegistry {

    /**
     * @param oAuth2Request
     */
    void addClient(String ticketId, OAuth2Request oAuth2Request);

    /**
     * @param ticketId
     * @return
     */
    List<OAuth2Request> getClients(String ticketId);
}

package com.hk.oauth2.logout;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author kevin
 * @date 2019-4-27 12:39
 */
public interface LogoutRequest extends Serializable {


    String getTicketId();

    /**
     * @return
     */
    String getClientId();

    /**
     * @return
     * @throws URISyntaxException
     */
    URI getLogoutUrl() throws URISyntaxException;

}

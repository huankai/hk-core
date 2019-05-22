package com.hk.oauth2.logout;

import java.io.Serializable;
import java.net.URL;

/**
 * @author kevin
 * @date 2019-5-18 13:59
 */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public interface LogoutRequest extends Serializable {

//    private String clientId;
//
//    private String logoutURL;

    /**
     * @return
     */
    LogoutRequestStatus getStatus();

    /**
     * Sets status of the request.
     *
     * @param status the status
     */
    void setStatus(LogoutRequestStatus status);

    String getClientId();

    void setClientId(String clientId);

    /**
     * Gets logout url.
     *
     * @return the logout url
     */
    URL getLogoutUrl();

}

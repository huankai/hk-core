package com.hk.oauth2.logout;

import java.net.URL;

import lombok.Getter;
import lombok.Setter;

/**
 * @author kevin
 * @date 2019-5-22 11:54
 */
@Getter
@Setter
@SuppressWarnings("serial")
public class DefaultLogoutRequest implements LogoutRequest {

    private URL logoutUrl;

    private String clientId;

    private LogoutRequestStatus status;

}

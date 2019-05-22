package com.hk.oauth2.logout;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;

/**
 * @author kevin
 * @date 2019-5-22 11:54
 */
@Getter
@Setter
public class DefaultLogoutRequest implements LogoutRequest {

    private URL logoutUrl;

    private String clientId;

    private LogoutRequestStatus status;

}

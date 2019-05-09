package com.hk.oauth2.logout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

/**
 * @author kevin
 * @date 2019-5-9 15:23
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleLogoutRequest implements LogoutRequest {

    private String ticketId;

    private String clientId;

    private URI logoutUrl;
}

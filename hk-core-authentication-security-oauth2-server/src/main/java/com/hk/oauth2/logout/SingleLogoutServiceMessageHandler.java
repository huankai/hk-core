package com.hk.oauth2.logout;

import org.springframework.security.core.Authentication;

/**
 * @author kevin
 * @date 2019-5-18 11:56
 */
public interface SingleLogoutServiceMessageHandler {

    void handle(Authentication authentication);
}

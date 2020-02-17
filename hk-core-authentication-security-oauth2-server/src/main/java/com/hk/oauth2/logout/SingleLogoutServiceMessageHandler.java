package com.hk.oauth2.logout;

/**
 * @author kevin
 * @date 2019-5-18 11:56
 */
@Deprecated
public interface SingleLogoutServiceMessageHandler {

    void handle(String tokenValue);
}

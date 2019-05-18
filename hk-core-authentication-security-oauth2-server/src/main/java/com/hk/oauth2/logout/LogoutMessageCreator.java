package com.hk.oauth2.logout;

/**
 * @author kevin
 * @date 2019-5-18 11:49
 */
public interface LogoutMessageCreator {

    String create(String accessToken);
}

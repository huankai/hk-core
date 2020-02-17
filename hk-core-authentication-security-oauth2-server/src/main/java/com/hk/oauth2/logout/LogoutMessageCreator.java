package com.hk.oauth2.logout;

/**
 * @author kevin
 * @date 2019-5-18 11:49
 */
@Deprecated
public interface LogoutMessageCreator {

    String create(LogoutRequest logoutRequest);
}

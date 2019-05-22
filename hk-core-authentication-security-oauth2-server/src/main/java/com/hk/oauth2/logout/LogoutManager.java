package com.hk.oauth2.logout;

import java.util.List;

/**
 * @author kevin
 * @date 2019-5-18 11:18
 */
public interface LogoutManager {

//    void registerLogoutClient(String accessToken, LogoutRequest logoutRequest);

    List<LogoutRequest> performLogout(String accessToken);

//    List<LogoutRequest> getClientsAndRemove(String accessToken);


    String createFrontChannelLogoutMessage(String accessToken);


}

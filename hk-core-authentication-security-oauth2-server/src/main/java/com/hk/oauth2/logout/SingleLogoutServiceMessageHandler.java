package com.hk.oauth2.logout;

import java.util.List;

/**
 * @author kevin
 * @date 2019-5-18 11:56
 */
@FunctionalInterface
public interface SingleLogoutServiceMessageHandler {

    List<LogoutRequest> handle(String accessToken);
}

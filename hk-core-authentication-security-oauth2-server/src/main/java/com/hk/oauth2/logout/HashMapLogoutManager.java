package com.hk.oauth2.logout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kevin
 * @date 2019-5-18 11:24
 */
public class HashMapLogoutManager implements LogoutManager {

    private static final Map<String, List<LogoutRequest>> clientMap = new HashMap<>();

    @Override
    public void registerLogoutClient(String accessToken, LogoutRequest logoutUri) {
        List<LogoutRequest> data = clientMap.getOrDefault(accessToken, new ArrayList<>());
        if (!data.contains(logoutUri)) {
            data.add(logoutUri);
        }
        clientMap.put(accessToken, data);
    }

    @Override
    public List<LogoutRequest> getClientsAndRemove(String accessToken) {
        try {
            return clientMap.getOrDefault(accessToken, new ArrayList<>());
        } finally {
            clientMap.remove(accessToken);
        }
    }
}

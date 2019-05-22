package com.hk.oauth2.logout;

import com.hk.commons.util.CompressionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin
 * @date 2019-5-18 11:24
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultLogoutManager implements LogoutManager {

    private final LogoutMessageCreator logoutMessageBuilder;

    private final SingleLogoutServiceMessageHandler singleLogoutServiceMessageHandler;

    private final boolean singleLogoutCallbacksDisabled;

//    private static final Map<String, List<LogoutRequest>> clientMap = new HashMap<>();
//
//    @Override
//    public void registerLogoutClient(String accessToken, LogoutRequest logoutUri) {
//        List<LogoutRequest> data = clientMap.getOrDefault(accessToken, new ArrayList<>());
//        if (!data.contains(logoutUri)) {
//            data.add(logoutUri);
//        }
//        clientMap.put(accessToken, data);
//    }
//
//    @Override
//    public List<LogoutRequest> getClientsAndRemove(String accessToken) {
//        try {
//            return clientMap.getOrDefault(accessToken, new ArrayList<>());
//        } finally {
//            clientMap.remove(accessToken);
//        }
//    }

    @Override
    public List<LogoutRequest> performLogout(String accessToken) {
        log.info("Performing logout operations for [{}]", accessToken);
        if (this.singleLogoutCallbacksDisabled) {
            log.info("Single logout callbacks are disabled");
            return new ArrayList<>(0);
        }
        return performLogoutForTicket(accessToken);
    }

    private List<LogoutRequest> performLogoutForTicket(String accessToken) {
        return singleLogoutServiceMessageHandler.handle(accessToken);
    }

    @Override
    public String createFrontChannelLogoutMessage(String accessToken) {
        String message = logoutMessageBuilder.create(accessToken);
        return CompressionUtils.compress(message);
    }
}

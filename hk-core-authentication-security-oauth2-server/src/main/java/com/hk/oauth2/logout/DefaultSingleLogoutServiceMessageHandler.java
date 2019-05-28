package com.hk.oauth2.logout;

import com.hk.commons.util.CollectionUtils;
import com.hk.oauth2.http.HttpClient;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author kevin
 * @date 2019-5-18 11:56
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultSingleLogoutServiceMessageHandler implements SingleLogoutServiceMessageHandler {

    private final HttpClient httpClient;

    private final LogoutManager logoutManager;

    private final SingleLogoutServiceLogoutUrlBuilder logoutServiceLogoutUrlBuilder;

    @Setter
    private LogoutMessageCreator logoutMessageCreator = new SamlCompliantLogoutMessageCreator();

    @Setter
    private boolean asynchronous = true;

    @Override
    public List<LogoutRequest> handle(String accessToken) {
//        String logoutMessage = logoutMessageCreator.create(accessToken);
//        List<LogoutRequest> clientLogoutUrl = logoutManager.getClientsAndRemove(accessToken);
//        clientLogoutUrl.forEach(item -> performBackChannelLogout(logoutMessage, item));
//        return createLogoutRequests(accessToken);
        Collection<URL> urls = logoutServiceLogoutUrlBuilder.determineLogoutUrl(accessToken);
        if (CollectionUtils.isEmpty(urls)) {
            log.debug("AccessToken [{}] does not support logout operations given no logout url could be determined.", accessToken);
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }

//    private boolean performBackChannelLogout(String logoutMessage, LogoutRequest logoutRequest) {
//        URL url = StringUtils.toURL(logoutRequest.getLogoutURL());
//        final LogoutHttpMessage msg = new LogoutHttpMessage(url, logoutMessage, asynchronous);
//        log.debug("Prepared logout message to send is [{}]. Sending...", msg);
//        return httpClient.sendMessageToEndPoint(msg);
//    }
}

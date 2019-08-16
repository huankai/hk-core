package com.hk.oauth2.logout;

import com.hk.oauth2.TokenRegistry;
import com.hk.oauth2.http.HttpClient;
import com.hk.oauth2.http.LogoutHttpMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author kevin
 * @date 2019-5-18 11:56
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultSingleLogoutServiceMessageHandler implements SingleLogoutServiceMessageHandler {

    private final HttpClient httpClient;

    private final TokenRegistry tokenRegistry;

    @Setter
    private LogoutMessageCreator logoutMessageCreator = new SamlCompliantLogoutMessageCreator();

    @Setter
    private boolean asynchronous = true;

    @Override
    public void handle(Authentication authentication) {
        List<LogoutRequest> logoutRequests = tokenRegistry.destroyAccessToken(authentication);
        logoutRequests.forEach(this::performBackChannelLogout);
    }

    private void performBackChannelLogout(LogoutRequest logoutRequest) {
        String logoutMessage = logoutMessageCreator.create(logoutRequest);
        try {
            URL url = new URL(logoutRequest.getLogoutURL());
            final LogoutHttpMessage msg = new LogoutHttpMessage(url, logoutMessage, asynchronous);
            log.debug("Prepared logout message to send is [{}]. Sending...", msg);
            httpClient.sendMessageToEndPoint(msg);
        } catch (MalformedURLException e) {
            log.warn("Can not create URL for LogoutUrL: {}", logoutRequest.getLogoutURL());
        }
    }
}

package com.hk.oauth2.logout;

import com.hk.commons.util.StringUtils;
import com.hk.oauth2.TokenRegistry;
import com.hk.oauth2.http.HttpClient;
import com.hk.oauth2.http.LogoutHttpMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author kevin
 * @date 2019-5-18 11:56
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultSingleLogoutServiceMessageHandler implements SingleLogoutServiceMessageHandler {

    private final HttpClient httpClient;

    private final TokenRegistry tokenRegistry;

    private final ConsumerTokenServices consumerTokenServices;

    @Setter
    private LogoutMessageCreator logoutMessageCreator = new SamlCompliantLogoutMessageCreator();

    @Setter
    private boolean asynchronous = true;

    @Override
    public void handle(Authentication authentication) {
        List<LogoutRequest> logoutRequests = tokenRegistry.destroyAccessToken(authentication);
        Set<String> revokeToken = new HashSet<>();
        for (LogoutRequest logoutRequest : logoutRequests) {
            if (!revokeToken.contains(logoutRequest.getAccessToken())) {
                consumerTokenServices.revokeToken(logoutRequest.getAccessToken());
                revokeToken.add(logoutRequest.getAccessToken());
            }
            performBackChannelLogout(logoutRequest);
        }
    }

    private void performBackChannelLogout(LogoutRequest logoutRequest) {
        String logoutMessage = logoutMessageCreator.create(logoutRequest);
        URL url = StringUtils.toURL(logoutRequest.getLogoutURL());
        final LogoutHttpMessage msg = new LogoutHttpMessage(url, logoutMessage, asynchronous);
        log.debug("Prepared logout message to send is [{}]. Sending...", msg);
        httpClient.sendMessageToEndPoint(msg);
    }
}

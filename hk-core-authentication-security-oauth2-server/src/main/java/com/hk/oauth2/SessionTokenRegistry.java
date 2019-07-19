package com.hk.oauth2;

import com.hk.commons.util.*;
import com.hk.core.authentication.oauth2.LogoutParameter;
import com.hk.oauth2.logout.LogoutRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * session token 存储
 */
@Slf4j
public class SessionTokenRegistry implements TokenRegistry, LogoutParameter {

    private final ConcurrentHashMap<String, List<LogoutRequest>> map = new ConcurrentHashMap<>(128);

    private final Lazy<ConsumerTokenServices> tokenServices = Lazy.of(() -> SpringContextHolder.getBean(ConsumerTokenServices.class));

    @Override
    public void addAccessToken(OAuth2Authentication authentication, OAuth2AccessToken accessToken) {
        OAuth2Request oAuth2Request = authentication.getOAuth2Request();
        Object details = authentication.getUserAuthentication().getDetails();
        if (details instanceof WebAuthenticationDetails) {
            String sessionId = ((WebAuthenticationDetails) details).getSessionId();
            List<LogoutRequest> logoutRequests = map.getOrDefault(sessionId, new ArrayList<>());
            Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
            String logoutUri = requestParameters.get(LOGOUT_PARAMETER_NAME);
            if (StringUtils.isNotEmpty(logoutUri)) {
                LogoutRequest logoutRequest = new LogoutRequest(oAuth2Request.getClientId(), logoutUri, accessToken.getValue());
                if (!logoutRequests.contains(logoutRequest)) {
                    log.debug("add logoutRequest: sessionId:[{}],logoutRequest:\n{}", sessionId, logoutRequest.toString());
                    logoutRequests.add(logoutRequest);
                }
                map.put(sessionId, logoutRequests);
            }
        }
    }

    @Override
    public List<LogoutRequest> destroyAccessToken(Authentication authentication) {
        Object details = authentication.getDetails();
        if (details instanceof WebAuthenticationDetails) {
            String sessionId = ((WebAuthenticationDetails) details).getSessionId();
            List<LogoutRequest> result = map.remove(sessionId);
            log.debug("destroy AccessToken,sessionId:[{}],logoutRequest:\n{}", sessionId, JsonUtils.serialize(result));
            destroyAccessToken(result);
        }
        return Collections.emptyList();
    }

    private void destroyAccessToken(List<LogoutRequest> logoutRequests) {
        if (CollectionUtils.isNotEmpty(logoutRequests)) {
            Set<String> tokenSet = logoutRequests
                    .stream()
                    .map(LogoutRequest::getAccessToken)
                    .collect(Collectors.toSet());
            for (String token : tokenSet) {
                tokenServices.get().revokeToken(token);
            }
        }
    }

    @Override
    public void deleteById(String id) {
        List<LogoutRequest> logoutRequests = map.remove(id);
        log.debug("session invalidate: delete LogoutRequest:\n{}", JsonUtils.serialize(logoutRequests));
        destroyAccessToken(logoutRequests);
    }
}

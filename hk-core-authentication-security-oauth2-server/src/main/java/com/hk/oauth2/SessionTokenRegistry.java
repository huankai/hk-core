package com.hk.oauth2;

import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.JsonUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.oauth2.LogoutParamater;
import com.hk.oauth2.logout.LogoutRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SessionTokenRegistry implements TokenRegistry, LogoutParamater {

    private final ConcurrentHashMap<String, List<LogoutRequest>> map = new ConcurrentHashMap<>(128);

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
            try {
                List<LogoutRequest> result = map.getOrDefault(sessionId, new ArrayList<>());
                log.debug("destroy AccessToken,sessionId:[{}],logoutRequest:\n{}", sessionId, JsonUtils.serialize(result));
                return result;
            } finally {
                map.remove(sessionId);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void deleteById(String id) {
        if (log.isDebugEnabled()) {
            List<LogoutRequest> logoutRequests = map.get(id);
            if (CollectionUtils.isNotEmpty(logoutRequests)) {
                log.debug("session invalidate: delete LogoutRequest:\n{}", JsonUtils.serialize(logoutRequests));
            }
        }
        map.remove(id);
    }
}

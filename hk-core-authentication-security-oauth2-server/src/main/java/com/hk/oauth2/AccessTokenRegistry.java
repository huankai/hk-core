package com.hk.oauth2;

import com.hk.commons.util.JsonUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.oauth2.LogoutParameter;
import com.hk.oauth2.logout.LogoutRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * access token 存储
 */
@Slf4j
public class AccessTokenRegistry implements TokenRegistry, LogoutParameter {

    private final ConcurrentHashMap<String, List<LogoutRequest>> map = new ConcurrentHashMap<>(128);

    @Override
    public void addAccessToken(OAuth2Authentication authentication, OAuth2AccessToken accessToken) {
        OAuth2Request oAuth2Request = authentication.getOAuth2Request();
        List<LogoutRequest> logoutRequests = map.getOrDefault(accessToken.getValue(), new ArrayList<>());
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        String logoutUri = requestParameters.get(LOGOUT_PARAMETER_NAME);
        if (StringUtils.isNotEmpty(logoutUri)) {
            LogoutRequest logoutRequest = new LogoutRequest(oAuth2Request.getClientId(), logoutUri, accessToken.getValue());
            logoutRequests.add(logoutRequest);
            log.debug("add logoutRequest: accessToken:[{}],logoutRequest:\n{}", accessToken.getValue(), logoutRequest.toString());
            map.put(accessToken.getValue(), logoutRequests);
        }
    }

    @Override
    public List<LogoutRequest> destroy(String tokenValue) {
        if (StringUtils.isNotEmpty(tokenValue)) {
            List<LogoutRequest> destroyLogoutRequest = map.remove(tokenValue);
            if (log.isDebugEnabled() && null != destroyLogoutRequest) {
                log.debug("destroy AccessToken,key:[{}],logoutRequest:\n{}", tokenValue, JsonUtils.serialize(destroyLogoutRequest));
            }
        }
        return null;
    }
}

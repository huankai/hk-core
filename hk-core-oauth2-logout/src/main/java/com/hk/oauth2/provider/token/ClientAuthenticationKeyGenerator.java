package com.hk.oauth2.provider.token;

import com.hk.core.web.Webs;
import com.hk.oauth2.logout.LogoutManager;
import com.hk.oauth2.logout.SimpleLogoutRequest;
import com.hk.oauth2.web.cookie.CookieProperties;
import com.hk.oauth2.web.cookie.CookieValueManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

/**
 * @author kevin
 * @date 2019-5-9 13:42
 * @see org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator
 */
@Slf4j
@RequiredArgsConstructor
@Deprecated
public class ClientAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

    private static final String CLIENT_ID = "client_id";

    private static final String SCOPE = "scope";

    private static final String USERNAME = "username";

    private static final String TICKET_ID = "ticket_id";

    private static final String LOGOUT_URL_KEY = "logout_url";

    private final CookieProperties cookieProperties;

    private final CookieValueManager cookieValueManager;

    private final LogoutManager logoutManager;

    public String extractKey(OAuth2Authentication authentication) {
        Map<String, String> values = new LinkedHashMap<>();
        OAuth2Request authorizationRequest = authentication.getOAuth2Request();
        if (!authentication.isClientOnly()) {
            values.put(USERNAME, authentication.getName());
        }
        values.put(CLIENT_ID, authorizationRequest.getClientId());
        if (authorizationRequest.getScope() != null) {
            values.put(SCOPE, OAuth2Utils.formatParameterList(new TreeSet<>(authorizationRequest.getScope())));
        }
        HttpServletRequest request = Webs.getHttpServletRequest();
        Cookie cookie = WebUtils.getCookie(request, cookieProperties.getName());
        if (Objects.nonNull(cookie)) {
            values.put(TICKET_ID, cookieValueManager.obtainCookieValue(cookie, request));
        }
        String key = generateKey(values);
        Map<String, String> requestParameters = authorizationRequest.getRequestParameters();
        if (requestParameters.containsKey(LOGOUT_URL_KEY)) {
            try {
                SimpleLogoutRequest logoutRequest = new SimpleLogoutRequest(key, authorizationRequest.getClientId(), new URI(requestParameters.get(LOGOUT_URL_KEY)));
//                logoutManager.
            } catch (URISyntaxException e) {
                log.warn(e.getMessage(), e);
            }
        }
//        logoutManager.
        return key;
    }

    protected String generateKey(Map<String, String> values) {
        return null;
//        MessageDigest digest;
//        try {
//            digest = MessageDigest.getInstance("MD5");
//            byte[] bytes = digest.digest(values.toString().getBytes(StandardCharsets.UTF_8));
//            return String.format("%032x", new BigInteger(1, bytes));
//        } catch (NoSuchAlgorithmException nsae) {
//            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).", nsae);
//        }
    }
}

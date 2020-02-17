package com.hk.core.authentication.oauth2.session;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.oauth2.LogoutParameter;
import com.hk.core.authentication.oauth2.utils.AccessTokenUtils;
import com.hk.core.authentication.oauth2.utils.XmlUtils;
import com.hk.core.web.Webs;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author kevin
 * @date 2019-5-18 10:26
 */
@Slf4j
@Deprecated
public class SingleSignOutHandler implements LogoutParameter {

    /**
     * session 中存储的 oauth2ClientContext
     */
    private static final String SESSION_OAUTH2_CLIENT_CONTEXT_KEY = "scopedTarget.oauth2ClientContext";

    private SessionMappingStorage sessionMappingStorage;

    private static final String LOGOUT_PARAM_NAME = CLIENT_LOGOUT_PARAMETER_NAME;

    @Setter
    private boolean eagerlyCreateSessions = true;

    private final String logoutUrl;

    private final LogoutStrategy logoutStrategy = isServlet30() ? HttpServletRequest::logout : request -> {
    };

    public SingleSignOutHandler(String logoutUrl, SessionMappingStorage sessionMappingStorage) {
        this.logoutUrl = logoutUrl;
        this.sessionMappingStorage = sessionMappingStorage;
    }

    public boolean process(final HttpServletRequest request) {
        if (isTokenRequest(request)) {
            log.trace("Received a token request");
            recordSession(request);
            return true;
        }
        if (isLogoutRequest(request)) {
            log.trace("Received a logout request");
            destroySession(request);
            return false;
        }
        log.trace("Ignoring URI for logout: {}", request.getRequestURI());
        return true;
    }

    /**
     * 销毁 session
     *
     * @param request request
     */
    private void destroySession(HttpServletRequest request) {
        var logoutMessage = request.getParameter(LOGOUT_PARAM_NAME);
        if (StringUtils.isEmpty(logoutMessage)) {
            log.error("Could not locate logout message of the request from {}", LOGOUT_PARAM_NAME);
            return;
        }
        log.trace("Logout request:\n{}", logoutMessage);
        final var token = XmlUtils.getTextForElement(logoutMessage, "AccessToken");
        if (StringUtils.isNotEmpty(token)) {
            final var session = sessionMappingStorage.removeSessionByMappingId(token);
            if (session != null) {
                final var sessionId = session.getId();
                log.debug("Invalidating session [{}] for token [{}]", sessionId, token);
                try {
                    session.invalidate();
                } catch (final IllegalStateException e) {
                    // ignore
                }
                try {
                    logoutStrategy.logout(request);
                } catch (ServletException e) {
                    log.debug("Error performing request.logout.");
                }
            }
        }
    }

    /**
     * 判断是否为 logout 请求
     *
     * @param request request
     * @return
     */
    private boolean isLogoutRequest(HttpServletRequest request) {
        return StringUtils.equals(StringUtils.substringAfter(request.getRequestURI(), request.getContextPath()), logoutUrl)
                && StringUtils.isNotEmpty(request.getParameter(LOGOUT_PARAM_NAME));
    }

    /**
     * 更新 {@link sessionMappingStorage} 中的 session
     *
     * @param request request
     */
    private void recordSession(HttpServletRequest request) {
        final var session = request.getSession(eagerlyCreateSessions);
        if (session == null) {
            log.debug("No session currently exists (and none created).  Cannot record session information for single sign out.");
            return;
        }
        var token = AccessTokenUtils.getAccessToken(request);
        if (StringUtils.isEmpty(token)) {
            var clientContext = Webs.getAttributeFromSession(SESSION_OAUTH2_CLIENT_CONTEXT_KEY, DefaultOAuth2ClientContext.class);
            token = clientContext.getAccessToken().getValue();
        }
        log.debug("Recording session for token {}", token);
        try {
            sessionMappingStorage.removeBySessionById(session.getId());
        } catch (final Exception e) {
            // ignore if the session is already marked as invalid. Nothing we can do!
        }
        sessionMappingStorage.addSessionById(token, session);
    }

    /**
     * 查看是否能获取到 access_token
     *
     * @param request request
     * @return
     */
    private boolean isTokenRequest(HttpServletRequest request) {
        if (StringUtils.isNotEmpty(AccessTokenUtils.getAccessToken(request))) {
            return true;
        }
        var session = request.getSession(eagerlyCreateSessions);
        if (session == null) {
            return false;
        }
        var clientContext = Webs.getAttributeFromSession(SESSION_OAUTH2_CLIENT_CONTEXT_KEY, DefaultOAuth2ClientContext.class);
        return null != clientContext && null != clientContext.getAccessToken();
    }

    private static boolean isServlet30() {
        try {
            return HttpServletRequest.class.getMethod("logout") != null;
        } catch (final NoSuchMethodException e) {
            return false;
        }
    }

    @FunctionalInterface
    private interface LogoutStrategy {

        void logout(HttpServletRequest request) throws ServletException;
    }
}

package com.hk.core.authentication.oauth2.session;

import com.hk.commons.util.StringUtils;
import com.hk.commons.util.XmlUtils;
import com.hk.core.authentication.oauth2.utils.AccessTokenUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author kevin
 * @date 2019-5-18 10:26
 */
@Slf4j
public class SingleSignOutHandler {

    private SessionMappingStorage sessionMappingStorage;

    private static final String LOGOUT_PARAM_NAME = "oauth2LogoutParameter";

    @Setter
    private boolean eagerlyCreateSessions = false;

    private final LogoutStrategy logoutStrategy = isServlet30() ? new Servlet30LogoutStrategy() : new Servlet25LogoutStrategy();

    public SingleSignOutHandler(SessionMappingStorage sessionMappingStorage) {
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

    private void destroySession(HttpServletRequest request) {
        String logoutMessage = request.getParameter(LOGOUT_PARAM_NAME);
        if (StringUtils.isEmpty(logoutMessage)) {
            log.error("Could not locate logout message of the request from {}", LOGOUT_PARAM_NAME);
            return;
        }
        log.trace("Logout request:\n{}", logoutMessage);
        final String token = XmlUtils.getTextForElement(logoutMessage, "AccessToken");
        if (StringUtils.isNotEmpty(token)) {
            final HttpSession session = sessionMappingStorage.removeSessionByMappingId(token);
            if (session != null) {
                final String sessionId = session.getId();
                log.debug("Invalidating session [{}] for token [{}]", sessionId, token);
                try {
                    session.invalidate();
                } catch (final IllegalStateException e) {
                    log.debug("Error invalidating session.", e);
                }
                this.logoutStrategy.logout(request);
            }
        }
    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        return StringUtils.isNotEmpty(request.getParameter(LOGOUT_PARAM_NAME));
    }

    private void recordSession(HttpServletRequest request) {
        final HttpSession session = request.getSession(eagerlyCreateSessions);
        if (session == null) {
            log.debug("No session currently exists (and none created).  Cannot record session information for single sign out.");
            return;
        }
        final String token = AccessTokenUtils.getAccessToken(request);
        log.debug("Recording session for token {}", token);
        try {
            sessionMappingStorage.removeBySessionById(session.getId());
        } catch (final Exception e) {
            // ignore if the session is already marked as invalid. Nothing we can do!
        }
        sessionMappingStorage.addSessionById(token, session);
    }

    private boolean isTokenRequest(HttpServletRequest request) {
        return StringUtils.isNotEmpty(AccessTokenUtils.getAccessToken(request));
    }

    private static boolean isServlet30() {
        try {
            return HttpServletRequest.class.getMethod("logout") != null;
        } catch (final NoSuchMethodException e) {
            return false;
        }
    }

    private interface LogoutStrategy {

        void logout(HttpServletRequest request);
    }

    private class Servlet25LogoutStrategy implements LogoutStrategy {

        public void logout(final HttpServletRequest request) {
            // nothing additional to do here
        }
    }

    private class Servlet30LogoutStrategy implements LogoutStrategy {

        public void logout(final HttpServletRequest request) {
            try {
                request.logout();
            } catch (final ServletException e) {
                log.debug("Error performing request.logout.");
            }
        }
    }
}

package com.hk.core.authentication.oauth2.session;

import com.hk.commons.util.Contants;
import com.hk.commons.util.StringUtils;
import com.hk.commons.util.XmlUtils;
import com.hk.core.authentication.oauth2.configuration.ConfigurationKeys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.util.zip.Inflater;

/**
 * @author kevin
 * @date 2019-5-6 12:53
 */
@Slf4j
public final class SingleSignOutHandler {

    private final static int DECOMPRESSION_FACTOR = 10;

    /**
     *
     */
    @Setter
    @Getter
    private SessionMappingStorage sessionMappingStorage = new HashMapBackedSessionMappingStorage();

    /**
     *
     */
    @Setter
    private String logoutParameterName = ConfigurationKeys.LOGOUT_PARAMETER_NAME.getName();

    /**
     *
     */
    @Setter
    private String artifactParameterName = ConfigurationKeys.ARTIFACT_PARAMETER_NAME.getName();

    /**
     * 是否创建 Session
     */
    @Setter
    private boolean eagerlyCreateSessions = true;

    private final LogoutStrategy logoutStrategy = isServlet30() ? new Servlet30LogoutStrategy() : new Servlet25LogoutStrategy();

    public synchronized void init() {

    }

    /**
     * @param request
     * @return
     */
    public boolean process(final HttpServletRequest request) {
        try {
            if (isTokenRequest(request)) {
                log.trace("Received a token request");
                recordSession(request);
                return false;
            }
        } catch (ServletRequestBindingException e) {
            return false;
        }

        if (isLogoutRequest(request)) {
            log.trace("Received a logout request");
            destroySession(request);
            return true;
        }
        log.trace("Ignoring URI for logout: {}", request.getRequestURI());
        return true;
    }

    private void destroySession(HttpServletRequest request) {
        String logoutMessage = request.getParameter(logoutParameterName);
        if (StringUtils.isEmpty(logoutMessage)) {
            log.error("Could not locate logout message of the request from {}", this.logoutParameterName);
            return;
        }
        if (!logoutMessage.contains("SessionIndex")) {
            logoutMessage = unCompressLogoutMessage(logoutMessage);
        }

        log.trace("Logout request:\n{}", logoutMessage);
        final String token = XmlUtils.getTextForElement(logoutMessage, "SessionIndex");
        if (StringUtils.isNotEmpty(token)) {
            final HttpSession session = this.sessionMappingStorage.removeSessionByMappingId(token);
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

    private String unCompressLogoutMessage(final String originalMessage) {
        final byte[] binaryMessage = DatatypeConverter.parseBase64Binary(originalMessage);
        Inflater deCompresser = null;
        try {
            // decompress the bytes
            deCompresser = new Inflater();
            deCompresser.setInput(binaryMessage);
            final byte[] result = new byte[binaryMessage.length * DECOMPRESSION_FACTOR];
            final int resultLength = deCompresser.inflate(result);
            // decode the bytes into a String
            return new String(result, 0, resultLength, Contants.CHARSET_UTF_8);
        } catch (final Exception e) {
            log.error("Unable to decompress logout message", e);
            throw new RuntimeException(e);
        } finally {
            if (deCompresser != null) {
                deCompresser.end();
            }
        }
    }

    /**
     * 判断当前请求是否为 logout 请求
     *
     * @param request request
     */
    private boolean isLogoutRequest(HttpServletRequest request) {
        return StringUtils.isNotEmpty(request.getParameter(logoutParameterName));
    }

    private void recordSession(HttpServletRequest request) {
        final HttpSession session = request.getSession(this.eagerlyCreateSessions);
        if (session == null) {
            log.debug("No session currently exists (and none created).  Cannot record session information for single sign out.");
            return;
        }
        final String token = request.getParameter(artifactParameterName);
        log.debug("Recording session for token {}", token);
        try {
            this.sessionMappingStorage.removeBySessionById(session.getId());
        } catch (final Exception e) {
            // ignore if the session is already marked as invalid. Nothing we can do!
        }
        sessionMappingStorage.addSessionById(token, session);
    }

    private boolean isTokenRequest(HttpServletRequest request) throws ServletRequestBindingException {
        return StringUtils.isNotEmpty(ServletRequestUtils.getStringParameter(request, artifactParameterName));
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

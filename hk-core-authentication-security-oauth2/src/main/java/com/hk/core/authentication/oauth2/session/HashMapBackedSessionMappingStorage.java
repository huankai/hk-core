package com.hk.core.authentication.oauth2.session;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kevin
 * @date 2019-4-27 16:57
 */
@Slf4j
public class HashMapBackedSessionMappingStorage implements SessionMappingStorage {

    private final Map<String, HttpSession> MANAGED_SESSIONS = new HashMap<>();

    /**
     * Maps the Session ID to the key from the CAS Server.
     */
    private final Map<String, String> ID_TO_SESSION_KEY_MAPPING = new HashMap<>();

    @Override
    public synchronized HttpSession removeSessionByMappingId(String mappingId) {
        final HttpSession session = MANAGED_SESSIONS.get(mappingId);
        if (session != null) {
            removeBySessionById(session.getId());
        }
        return session;
    }

    @Override
    public synchronized void removeBySessionById(String sessionId) {
        log.debug("Attempting to remove Session=[{}]", sessionId);
        final String key = ID_TO_SESSION_KEY_MAPPING.get(sessionId);
        if (log.isDebugEnabled()) {
            if (key != null) {
                log.debug("Found mapping for session.  Session Removed.");
            } else {
                log.debug("No mapping for session found.  Ignoring.");
            }
        }
        MANAGED_SESSIONS.remove(key);
        ID_TO_SESSION_KEY_MAPPING.remove(sessionId);
    }

    @Override
    public synchronized void addSessionById(String mappingId, HttpSession session) {
        ID_TO_SESSION_KEY_MAPPING.put(session.getId(), mappingId);
        MANAGED_SESSIONS.put(mappingId, session);
    }
}
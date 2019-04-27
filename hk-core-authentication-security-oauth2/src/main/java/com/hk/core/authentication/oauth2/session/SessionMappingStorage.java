package com.hk.core.authentication.oauth2.session;

import javax.servlet.http.HttpSession;

/**
 * @author kevin
 * @date 2019-4-27 16:56
 */
public interface SessionMappingStorage {

    /**
     * Remove the HttpSession based on the mappingId.
     *
     * @param mappingId the id the session is keyed under.
     * @return the HttpSession if it exists.
     */
    HttpSession removeSessionByMappingId(String mappingId);

    /**
     * Remove a session by its Id.
     * @param sessionId the id of the session.
     */
    void removeBySessionById(String sessionId);

    /**
     * Add a session by its mapping Id.
     * @param mappingId the id to map the session to.
     * @param session the HttpSession.
     */
    void addSessionById(String mappingId, HttpSession session);
}

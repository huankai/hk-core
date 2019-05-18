package com.hk.core.authentication.oauth2.session;

import javax.servlet.http.HttpSession;

/**
 * @author kevin
 * @date 2019-5-18 10:06
 */
public interface SessionMappingStorage {

    HttpSession removeSessionByMappingId(String mappingId);

    void removeBySessionById(String sessionId);

    void addSessionById(String mappingId, HttpSession session);
}

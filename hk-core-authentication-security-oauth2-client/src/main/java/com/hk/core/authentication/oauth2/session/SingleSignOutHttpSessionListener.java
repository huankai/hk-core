package com.hk.core.authentication.oauth2.session;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 单点退出监听器
 *
 * @author kevin
 * @date 2019-5-18 10:16
 */
@Slf4j
public class SingleSignOutHttpSessionListener implements HttpSessionListener {

    private final SessionMappingStorage sessionMappingStorage;

    public SingleSignOutHttpSessionListener(SessionMappingStorage sessionMappingStorage) {
        this.sessionMappingStorage = sessionMappingStorage;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        var sessionId = event.getSession().getId();
        if (log.isDebugEnabled()) {
            log.debug("The session has been destroyed, SessionId: {}", sessionId);
        }
        sessionMappingStorage.removeBySessionById(sessionId);
    }
}

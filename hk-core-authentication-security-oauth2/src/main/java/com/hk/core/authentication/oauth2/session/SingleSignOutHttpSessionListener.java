package com.hk.core.authentication.oauth2.session;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * oauth2 Session 监听器
 *
 * @author kevin
 * @date 2019-5-6 13:22
 */
@RequiredArgsConstructor
public final class SingleSignOutHttpSessionListener implements HttpSessionListener {

    private final SessionMappingStorage sessionMappingStorage;

    /**
     * session 失效时 将session 删除
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        sessionMappingStorage.removeBySessionById(event.getSession().getId());
    }
}

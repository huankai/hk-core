package com.hk.oauth2;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@RequiredArgsConstructor
public class InvalidHttpSessionListener implements HttpSessionListener {

    private final TokenRegistry tokenRegistry;

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        tokenRegistry.deleteById(sessionId);
    }
}

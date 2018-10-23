package com.hk.message.websocket.handlers;

import com.hk.core.authentication.api.SecurityContextUtils;
import com.hk.core.authentication.api.UserPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * @author: huangkai
 * @date: 2018-9-21 20:30
 */
public class PrincipalHandshakeHandler extends DefaultHandshakeHandler {

    @Nullable
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        UserPrincipal principal = SecurityContextUtils.getPrincipal();
        return new WebSocketUserPrincipal(principal.getUserId());
    }

    private class WebSocketUserPrincipal implements Principal {

        private final String userId;

        WebSocketUserPrincipal(String userId) {
            this.userId = userId;
        }

        @Override
        public String getName() {
            return userId;
        }
    }
}

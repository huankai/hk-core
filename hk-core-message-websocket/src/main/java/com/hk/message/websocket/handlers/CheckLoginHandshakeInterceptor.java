package com.hk.message.websocket.handlers;

import com.hk.commons.util.AssertUtils;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.SecurityContextUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 握手之前登陆检查，如果没有登陆，不能握手成功
 *
 * @author: huangkai
 * @date: 2018-9-21 21:26
 */
public class CheckLoginHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        return SecurityContextUtils.isAuthenticated();
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }

}

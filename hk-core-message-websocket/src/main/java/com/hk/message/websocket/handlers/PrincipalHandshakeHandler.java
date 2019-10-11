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
 * 获取 websocket 用户，不建议这么做了， spring security messaging 有集成
 *
 * @author huangkai
 * @date 2018-9-21 20:30
 */
@Deprecated
public class PrincipalHandshakeHandler extends DefaultHandshakeHandler {

    @Nullable
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        return new WebSocketUserPrincipal(SecurityContextUtils.getPrincipal());
    }

    @SuppressWarnings("serial")
    public class WebSocketUserPrincipal extends UserPrincipal implements Principal {

        WebSocketUserPrincipal(UserPrincipal userPrincipal) {
            super(userPrincipal.getUserId(), userPrincipal.getAccount(), userPrincipal.getRealName(), userPrincipal.getUserType(), userPrincipal.getPhone(),
                    userPrincipal.getEmail(), userPrincipal.getSex(), userPrincipal.getIconPath(), userPrincipal.getRoles(), userPrincipal.getPermissions());
        }

        @Override
        public String getName() {
            return super.getRealName();
        }
    }
}

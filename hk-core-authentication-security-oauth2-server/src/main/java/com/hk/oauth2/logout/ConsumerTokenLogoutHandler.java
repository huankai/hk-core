package com.hk.oauth2.logout;

import com.hk.oauth2.session.AbstractRedisSessionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;


/**
 * 单点退出处理器，退出后，将token 信息清除
 */
@Slf4j
@Deprecated
public class ConsumerTokenLogoutHandler extends AbstractRedisSessionHandler implements LogoutHandler {

    public ConsumerTokenLogoutHandler(ConsumerTokenServices consumerTokenServices,
                                      RedisConnectionFactory redisConnectionFactory) {
        super(consumerTokenServices, redisConnectionFactory);
    }

    /**
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession(false);
        if (Objects.nonNull(session)) {
            destroyed(session.getId());
        }
    }
}

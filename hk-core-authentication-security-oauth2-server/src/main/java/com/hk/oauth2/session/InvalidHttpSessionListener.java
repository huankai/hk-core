package com.hk.oauth2.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author kevin
 * @date 2020-02-14 11:27
 */
@Slf4j
public class InvalidHttpSessionListener extends AbstractRedisSessionHandler implements HttpSessionListener {

    public InvalidHttpSessionListener(ConsumerTokenServices consumerTokenServices,
                                      RedisConnectionFactory redisConnectionFactory) {
        super(consumerTokenServices, redisConnectionFactory);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        log.debug("sessionDestroyed:{} ", sessionId);
        destroyed(sessionId);

    }
}

package com.hk.oauth2.logout;

import com.hk.commons.util.StringUtils;
import com.hk.oauth2.provider.token.store.CustomRedisTokenStore;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;


/**
 * 单点退出处理器，退出后，将token 信息清除
 */
@Slf4j
@RequiredArgsConstructor
public class ConsumerTokenLogoutHandler implements LogoutHandler {

    private final ConsumerTokenServices consumerTokenServices;

    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * @see com.hk.oauth2.provider.token.store.CustomRedisTokenStore#serializationStrategy
     */
    @Setter
    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    /**
     * @param request
     * @param response
     * @param authentication
     * @see CustomRedisTokenStore#storeAccessToken
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession(false);
        if (Objects.nonNull(session)) {
            byte[] key = serializationStrategy.serialize(CustomRedisTokenStore.AUTH_SESSION + session.getId());
            RedisConnection connection = redisConnectionFactory.getConnection();
            try {
                connection.openPipeline();
                connection.get(key);
                connection.del(key);
                List<Object> results = connection.closePipeline();
                byte[] bytes = (byte[]) results.get(0);
                String token = serializationStrategy.deserializeString(bytes);
                if (StringUtils.isNotEmpty(token)) {
                    consumerTokenServices.revokeToken(token);
                }
            } finally {
                connection.close();
            }

        }
    }
}

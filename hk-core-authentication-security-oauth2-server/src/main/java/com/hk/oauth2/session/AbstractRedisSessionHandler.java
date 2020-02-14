package com.hk.oauth2.session;

import com.hk.commons.util.StringUtils;
import com.hk.oauth2.provider.token.store.CustomRedisTokenStore;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

import java.util.List;

/**
 * @author kevin
 * @date 2020-02-14 11:30
 */
@RequiredArgsConstructor
public abstract class AbstractRedisSessionHandler {

    private final ConsumerTokenServices consumerTokenServices;

    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * @see com.hk.oauth2.provider.token.store.CustomRedisTokenStore#serializationStrategy
     */
    @Setter
    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    protected final void destroyed(String sessionId) {
        byte[] key = serializationStrategy.serialize(CustomRedisTokenStore.AUTH_SESSION + sessionId);
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

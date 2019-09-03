package com.hk.core.authentication.api.validatecode;

import com.hk.commons.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.context.request.RequestAttributes;

import java.nio.charset.StandardCharsets;

/**
 * @author kevin
 * @date 2019-9-3 16:17
 */
@RequiredArgsConstructor
public class RedisCacheValidateCodeStrategy implements ValidateCodeStrategy {

    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    public <C extends ValidateCode> void save(RequestAttributes request, String name, C validateCode) {
        RedisConnection connection = redisConnectionFactory.getConnection();
        try {
            connection.setEx(name.getBytes(StandardCharsets.UTF_8), validateCode.getExpireSecond(),
                    JsonUtils.serialize(validateCode).getBytes(StandardCharsets.UTF_8));
        } finally {
            connection.close();
        }
    }

    @Override
    public <C extends ValidateCode> C get(RequestAttributes request, String name, Class<C> clazz) {
        RedisConnection connection = redisConnectionFactory.getConnection();
        try {
            byte[] values = connection.get(name.getBytes(StandardCharsets.UTF_8));
            if (values == null) {
                return null;
            }
            String str = new String(values, StandardCharsets.UTF_8);
            return JsonUtils.deserialize(str, clazz);
        } finally {
            connection.close();
        }
    }

    @Override
    public void remove(RequestAttributes request, String name) {
        RedisConnection connection = redisConnectionFactory.getConnection();
        try {
            connection.del(name.getBytes(StandardCharsets.UTF_8));
        } finally {
            connection.close();
        }
    }
}

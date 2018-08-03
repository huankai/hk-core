package com.hk.core.autoconfigure.cache.redis;

import java.util.Objects;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.Contants;
import com.hk.commons.util.JsonUtils;

/**
 * @author: kevin
 * @date 2018-05-31 17:45
 */
@Configuration
@ConditionalOnClass(RedisTemplate.class)
public class CacheRedisAutoConfiguration {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        GenericFastJson2JsonRedisSerializer<?> valueSerializer = new GenericFastJson2JsonRedisSerializer<>();

        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);

        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        return redisTemplate;

    }

    private static class GenericFastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {


        @Override
        public byte[] serialize(T t) throws SerializationException {
            if (null == t) {
                return new byte[0];
            }
            JsonWraper<T> wraper = new JsonWraper<>(t);
            return JsonUtils.serialize(wraper).getBytes(Contants.CHARSET_UTF_8);
        }

        @Override
        @SuppressWarnings("unchecked")
        public T deserialize(byte[] bytes) throws SerializationException {
            if (ArrayUtils.isEmpty(bytes)) {
                return null;
            }
            String jsonString = new String(bytes, Contants.CHARSET_UTF_8);
            return (T) Objects.requireNonNull(JsonUtils.deserialize(jsonString, JsonWraper.class), "Json String to Object is Null:" + jsonString).getValue();
        }
    }

    private static class JsonWraper<T> {

        private T value;

        public JsonWraper(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }
}

package com.hk.core.autoconfigure.cache.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hk.commons.util.JsonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;


/**
 * @author: kevin
 * @date: 2018-05-31 17:45
 */
@Configuration
@ConditionalOnClass(RedisTemplate.class)
public class CacheRedisAutoConfiguration {

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setEnableTransactionSupport(true);

        StringRedisSerializer keySerializer = new StringRedisSerializer();

        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .defaultUseWrapper(true)
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        JsonUtils.configure(objectMapper);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(redisSerializer);

        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        return redisTemplate;

    }

//    private static class GenericJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
//
//
//        @Override
//        public byte[] serialize(T t) throws SerializationException {
//            if (null == t) {
//                return new byte[0];
//            }
//            return JsonUtils.serialize(t).getBytes(Contants.CHARSET_UTF_8);
//        }
//
//        @Override
//        @SuppressWarnings("unchecked")
//        public T deserialize(byte[] bytes) throws SerializationException {
//            if (ArrayUtils.isEmpty(bytes)) {
//                return null;
//            }
//            String jsonString = new String(bytes, Contants.CHARSET_UTF_8);
//            return (T) JsonUtils.deserialize(jsonString, Object.class);
////            return (T) Objects.requireNonNull(JsonUtils.deserialize(jsonString, JsonWraper.class), "Json String to Object is Null:" + jsonString).getValue();
//        }
//    }
}

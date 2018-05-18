package com.hk.core.cache.redis;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hk.commons.fastjson.JsonUtils;
import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.Contants;
import com.hk.commons.util.date.DatePattern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author: huangkai
 * @date 2018-05-18 12:25
 */
@Configuration
public class CacheReidsAutoConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
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

    private class GenericFastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

        /**
         * <p>
         * fastjson autoType
         * </p>
         * https://github.com/alibaba/fastjson/wiki/enable_autotype
         *
         */
        private String acceptBasePackage = "com.hk";

        @Override
        public byte[] serialize(T t) throws SerializationException {
            if (null == t) {
                return new byte[0];
            }
            FastJsonWraper<T> wraper = new FastJsonWraper<>(t);
            return JsonUtils.toJSONString(wraper, true, DatePattern.YYYY_MM_DD_HH_MM_SS, null, SerializerFeature.WriteClassName).getBytes(Contants.CHARSET_UTF_8);
        }

        @Override
        @SuppressWarnings("unchecked")
        public T deserialize(byte[] bytes) throws SerializationException {
            if (ArrayUtils.isEmpty(bytes)) {
                return null;
            }
            String jsonString = new String(bytes, Contants.CHARSET_UTF_8);
            ParserConfig.getGlobalInstance().addAccept(acceptBasePackage);
            FastJsonWraper<T> wraper = JsonUtils.parseObject(jsonString, FastJsonWraper.class);
            return wraper.getValue();
        }
    }

    private static class FastJsonWraper<T> {

        private T value;

        public FastJsonWraper() {

        }

        public FastJsonWraper(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}

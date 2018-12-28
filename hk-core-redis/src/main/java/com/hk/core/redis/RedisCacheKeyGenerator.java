package com.hk.core.redis;

import com.hk.commons.util.ClassUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * @author huangkai
 * @date 2018-12-27 11:33
 */
public class RedisCacheKeyGenerator implements KeyGenerator {

    private static Logger logger = LoggerFactory.getLogger(RedisCacheKeyGenerator.class);

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append(target.getClass().getSimpleName()).append(".").append(method.getName()).append(":");
//        key.append("length=").append(params.length).append("|");
        for (Object param : params) {
            if (null != param) {
                if (ClassUtils.isPrimitiveArray(param.getClass())
                        || ClassUtils.isPrimitiveWrapperArray(param.getClass())
                        || param instanceof String[]) {
                    key.append(ArrayUtils.toString(param));
                } else if (ClassUtils.isPrimitiveOrWrapper(param.getClass())
                        || param instanceof String) {
                    key.append(param);
                } else {
                    logger.warn("Using an object as a cache key may lead to unexpected results. " +
                            "Either use @Cacheable(key=..) or implement CacheKey. Method is " + target.getClass() + "#" + method.getName());
                    key.append(param.hashCode());
                }
            }
            key.append('|');
        }

        return key.toString();
    }
}

package com.hk.core.authentication.api.validatecode;

import lombok.Setter;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.web.context.request.RequestAttributes;

/**
 * @author kevin
 * @date 2018-07-27 14:09
 */
public class SpringCacheValidateCodeStrategy implements ValidateCodeStrategy {

    @Setter
    private Cache cache = new ConcurrentMapCache("ValidateCodeStrategy");

    @Override
    public void save(RequestAttributes request, String name, Object value) {
        cache.put(name, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(RequestAttributes request, String name) {
        Cache.ValueWrapper valueWrapper = cache.get(name);
        return valueWrapper == null ? null : (T) valueWrapper.get();
    }

    @Override
    public void remove(RequestAttributes request, String name) {
        cache.evict(name);
    }
}

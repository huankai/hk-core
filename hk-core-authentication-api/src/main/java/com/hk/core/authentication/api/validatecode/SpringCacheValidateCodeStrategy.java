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
    public <C extends ValidateCode> void save(RequestAttributes request, String name, C value) {
        cache.put(name, value);
    }

    @Override
    public <C extends ValidateCode> C get(RequestAttributes request, String name, Class<C> clazz) {
        Cache.ValueWrapper valueWrapper = cache.get(name);
        return valueWrapper == null ? null : clazz.cast(valueWrapper.get());
    }

    @Override
    public void remove(RequestAttributes request, String name) {
        cache.evict(name);
    }
}

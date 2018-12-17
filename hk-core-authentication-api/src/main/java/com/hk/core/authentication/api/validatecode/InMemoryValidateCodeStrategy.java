package com.hk.core.authentication.api.validatecode;

import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kevin
 * @date 2018-07-27 14:09
 */
public class InMemoryValidateCodeStrategy implements ValidateCodeStrategy {

    public static InMemoryValidateCodeStrategy INSTANCE = new InMemoryValidateCodeStrategy();

    private static final Map<String, Object> MAP = new ConcurrentHashMap<>(128);

    private InMemoryValidateCodeStrategy() {

    }

    @Override
    public void save(RequestAttributes request, String name, Object value) {
        MAP.put(name, value);
    }

    @Override
    public Object get(RequestAttributes request, String name) {
        return MAP.get(name);
    }

    @Override
    public void remove(RequestAttributes request, String name) {
        MAP.remove(name);

    }
}

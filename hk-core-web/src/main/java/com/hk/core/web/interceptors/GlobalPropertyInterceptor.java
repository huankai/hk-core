package com.hk.core.web.interceptors;

import com.hk.commons.util.CollectionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 属性拦截器，将Map 属性保存到request中
 *
 * @author huangkai
 * @date 2018-11-30 09:27
 */
public class GlobalPropertyInterceptor extends HandlerInterceptorAdapter {

    /**
     * request 中的属性
     *
     * @see RequestPropertyProperties#property
     */
    private Map<String, Object> property = new HashMap<>();

    public void setProperty(Map<String, Object> property) {
        if (CollectionUtils.isNotEmpty(property)) {
            this.property.putAll(property);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        property.forEach(request::setAttribute);
        return true;
    }

    /**
     * request属性
     */
    @ConfigurationProperties(prefix = "hk.request")
    public static class RequestPropertyProperties {
    	
        private Map<String, Object> property;

        public Map<String, Object> getProperty() {
            return property;
        }

        public void setProperty(Map<String, Object> property) {
            this.property = property;
        }
    }
}

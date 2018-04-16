package com.hk.core.web.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hk.commons.converters.*;
import com.hk.commons.util.SpringContextHolder;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.web.ServletContextHolder;
import com.hk.core.web.interceptors.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author huangkai
 * @date 2017年9月27日上午10:54:04
 */
@Configuration
public class WebMVCConfigutation extends WebMvcConfigurerAdapter {

    /**
     * <pre>
     * 静态资源地址
     * </pre>
     */
    @Value("${hk.web.resuources.url:/}")
    private String resourceUrl;

    @Autowired
    private SecurityContext securityContext;

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    public ServletContextHolder servletContextHolder() {
        return new ServletContextHolder();
    }

    /**
     * 添加fastJson
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        /* String 返回为 json对象*/
        converters.forEach(item -> {
            if (item instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) item).setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON_UTF8));
            }
        });

//        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
//        FastJsonConfig config = new FastJsonConfig();
//        // 这里不需要设置，默认使用 SerializerFeature.WriteDateUseDateFormat ，pattern = yyyy-MM-dd
//        // HH:mm:ss
//        // 如果需要指定 format ,可以在指定属性上添加注解 @JSONField(format = "yyyy-MM-dd HH:mm")来指定
//        // pattern
//        // config.setDateFormat(DatePattern.YYYY_MM_DD_HH_MM_SS.getPattern());
//        config.setSerializeConfig(JsonUtils.CONFIG);
//        config.setSerializerFeatures(JsonUtils.FEATURES);
//        converter.setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON_UTF8));
//        converter.setFastJsonConfig(config);
//        converters.add(converter);
    }

    /**
     * 添加转换
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToBooleanConverter());
        registry.addConverter(new StringToDateConverter());
        registry.addConverter(new StringToCalendarConverter());
        registry.addConverter(new StringToYearConverter());
        registry.addConverter(new StringToLocalDateConverter());
        registry.addConverter(new StringToLocalTimeConverter());
        registry.addConverter(new StringToLocalDateTimeConverter());
        registry.addConverter(new StringToBooleanConverter());
    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        RequestInterceptor interceptor = new RequestInterceptor();
        interceptor.setSecurityContext(securityContext);
        Map<String, String> maps = Maps.newHashMap();
        maps.put("resourceUrl", resourceUrl);
        interceptor.setProperties(maps);
        registry.addInterceptor(interceptor).addPathPatterns("/**");

        /* ****************** 国际化支持******************* */
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }

    /* ****************** 国际化支持******************* */
    @Bean
    public LocaleResolver LocaleResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.getDefault());
        return sessionLocaleResolver;
    }
}

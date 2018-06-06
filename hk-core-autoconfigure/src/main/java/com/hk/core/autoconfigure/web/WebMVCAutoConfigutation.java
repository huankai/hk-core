package com.hk.core.autoconfigure.web;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import com.hk.commons.converters.*;
import com.hk.commons.util.JsonUtils;
import com.hk.commons.util.SpringContextHolder;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.web.ServletContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
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

/**
 * @author: huangkai
 * @date 2018-05-31 16:26
 */
@Configuration
public class WebMVCAutoConfigutation extends WebMvcConfigurerAdapter {

    @Bean
    @ConditionalOnClass(SpringContextHolder.class)
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    @ConditionalOnWebApplication
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
                ((StringHttpMessageConverter) item).setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON_UTF8, MediaType.TEXT_PLAIN, MediaType.ALL));
            }
        });

        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        // 这里不需要设置，默认使用 SerializerFeature.WriteDateUseDateFormat ，pattern = yyyy-MM-dd
        // HH:mm:ss
        // 如果需要指定 format ,可以在指定属性上添加注解 @JSONField(format = "yyyy-MM-dd HH:mm")来指定
        // pattern
        // config.setDateFormat(DatePattern.YYYY_MM_DD_HH_MM_SS.getPattern());
        config.setSerializeConfig(JsonUtils.CONFIG);
        config.setSerializerFeatures(JsonUtils.FEATURES);
        converter.setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON_UTF8));
        converter.setFastJsonConfig(config);
        converters.add(converter);
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
        registry.addConverter(new StringToJodaDateTimeConverter());
    }

    @Autowired
    private SecurityContext securityContext;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        RequestInterceptor interceptor = new RequestInterceptor();
//        interceptor.setSecurityContext(securityContext);
//        Map<String, String> maps = Maps.newHashMap();
//        interceptor.setProperties(maps);
//        registry.addInterceptor(interceptor).addPathPatterns("/**");

        /* ****************** 国际化支持******************* */
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);

//        不同应用切换当前用户信息
//        registry.addInterceptor(new SecurityContextInterceptor(securityContext)).excludePathPatterns("/api/**");
    }

    /* ****************** 国际化支持******************* */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.getDefault());
        return sessionLocaleResolver;
    }
}

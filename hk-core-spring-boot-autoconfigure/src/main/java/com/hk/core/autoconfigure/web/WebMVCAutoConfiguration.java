package com.hk.core.autoconfigure.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hk.commons.converters.*;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.Contants;
import com.hk.commons.util.JsonUtils;
import com.hk.commons.util.SpringContextHolder;
import com.hk.core.web.ServletContextHolder;
import com.hk.core.web.interceptors.GlobalPropertyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.List;
import java.util.Locale;

/**
 * @author: kevin
 * @date: 2018-05-31 16:26
 */
@Configuration
@ServletComponentScan(basePackages = {"com.hk.core"})
@EnableConfigurationProperties(GlobalPropertyInterceptor.RequestPropertyProperties.class)
public class WebMVCAutoConfiguration implements WebMvcConfigurer {

    @Bean
    @ConditionalOnClass(SpringContextHolder.class)
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnClass(ServletContextHolder.class)
    public ServletContextHolder servletContextHolder() {
        return new ServletContextHolder();
    }

    @Autowired
    private GlobalPropertyInterceptor.RequestPropertyProperties requestProperty;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.forEach(converter -> {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(Contants.CHARSET_UTF_8);
            } else if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
                        .json()
                        .defaultUseWrapper(true)
                        .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                        .modules(JsonUtils.getJavaTimeModule())
                        .build();
                JsonUtils.configure(objectMapper);
                mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
                mappingJackson2HttpMessageConverter.setDefaultCharset(Contants.CHARSET_UTF_8);
            }
        });
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
    }


    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserContextInterceptor()).addPathPatterns("/**");
        if (CollectionUtils.isNotEmpty(requestProperty.getProperty())) {
            GlobalPropertyInterceptor propertyInterceptor = new GlobalPropertyInterceptor();
            propertyInterceptor.setProperty(requestProperty.getProperty());
            registry.addInterceptor(propertyInterceptor).addPathPatterns("/**");
        }

        /* ****************** 国际化支持******************* */
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }

    /* ****************** 国际化支持******************* */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.getDefault());
        return sessionLocaleResolver;
    }
}

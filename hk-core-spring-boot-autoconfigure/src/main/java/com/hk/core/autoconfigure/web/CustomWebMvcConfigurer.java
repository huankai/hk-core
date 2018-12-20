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
import com.hk.core.web.mvc.CustomRequestMappingHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

/**
 * @author huangkai
 * @date 2018-05-31 16:26
 */
@Configuration
@ServletComponentScan(basePackages = {"com.hk.core"})
@EnableConfigurationProperties(GlobalPropertyInterceptor.RequestPropertyProperties.class)
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

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

    /**
     * @return
     * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration#mvcRegistrations
     */
    @Bean
    public WebMvcRegistrations webMvcRegistrations() {
        return new WebMvcRegistrations() {

            /**
             * 重写  RequestMappingHandlerMapping
             * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration#createRequestMappingHandlerMapping
             */
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new CustomRequestMappingHandlerMapping();
            }
        };
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.forEach(converter -> {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(Contants.CHARSET_UTF_8);
            } else if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().defaultUseWrapper(true)
                        .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                        .modules(JsonUtils.modules()).build();
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

}
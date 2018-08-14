package com.hk.core.autoconfigure.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.hk.commons.converters.*;
import com.hk.commons.util.Contants;
import com.hk.commons.util.SpringContextHolder;
import com.hk.commons.util.date.DatePattern;
import com.hk.core.web.ServletContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author: kevin
 * @date 2018-05-31 16:26
 */
@Configuration
@ServletComponentScan(basePackages = {"com.hk.core"})
@ComponentScan("com.hk.core.autoconfigure.exception")
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

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.forEach(converter -> {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(Contants.CHARSET_UTF_8);
            }
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                JavaTimeModule module = new JavaTimeModule();
                module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD_HH_MM_SS.getPattern())));
                module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD_HH_MM_SS.getPattern())));

                module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD.getPattern())));
                module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD.getPattern())));

                module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.HH_MM_SS.getPattern())));
                module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.HH_MM_SS.getPattern())));
                ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
                        .json()
                        .simpleDateFormat(DatePattern.YYYY_MM_DD_HH_MM_SS.getPattern())
                        .defaultUseWrapper(true)
                        .modules(module)
                        .failOnUnknownProperties(false)
                        .build();
                mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
                mappingJackson2HttpMessageConverter.setDefaultCharset(Contants.CHARSET_UTF_8);
                List<MediaType> mediaTypeList = new ArrayList<>();
                mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
                mappingJackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypeList);

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

//    @Autowired
//    private SecurityContext securityContext;

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

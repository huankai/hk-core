package com.hk.core.autoconfigure.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.hk.commons.converters.*;
import com.hk.commons.util.*;
import com.hk.commons.util.date.DatePattern;
import com.hk.core.jdbc.deserializer.ConditionQueryModelDeserializer;
import com.hk.core.jdbc.query.ConditionQueryModel;
import com.hk.core.web.ServletContextHolder;
import com.hk.core.web.filter.XssFilter;
import com.hk.core.web.interceptors.GlobalPropertyInterceptor;
import com.hk.core.web.mvc.CustomRequestMappingHandlerMapping;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.Filter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author huangkai
 * @date 2018-05-31 16:26
 */
@Configuration
@EnableConfigurationProperties(GlobalPropertyInterceptor.RequestPropertyProperties.class)
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    @ConditionalOnWebApplication
    public ServletContextHolder servletContextHolder() {
        return new ServletContextHolder();
    }

    /**
     * <pre>
     * spring 会自动 注入{@link ObjectMapper},但此 bean在序列化 与反序列化 json 时，不支持 JDK 8的 日期 API，
     * 所在这里配置 支持JDK 8 的日期 API 功能，以及其它功能
     * </pre>
     * <pre>
     *     在使用 spring cloud stream 接受消息时,也会使用 {@link org.springframework.cloud.stream.converter.ApplicationJsonMessageMarshallingConverter#objectMapper}
     *     进行序列化,此对象配置在 {@link org.springframework.cloud.stream.config.ContentTypeConfiguration#compositeMessageConverterFactory(ObjectProvider, List)}
     * </pre>
     *
     * @see org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperConfiguration#jacksonObjectMapper(Jackson2ObjectMapperBuilder)
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter(JsonUtils.IGNORE_ENTITY_SERIALIZE_FIELD_FILTER_ID,
                    SimpleBeanPropertyFilter.serializeAllExcept(AuditField.AUDIT_FIELD_ARRAY));
            jacksonObjectMapperBuilder.modules(JsonUtils.modules())
                    .filters(filterProvider)
                    .deserializerByType(ConditionQueryModel.class, new ConditionQueryModelDeserializer())
                    .dateFormat(new SimpleDateFormat(DatePattern.YYYY_MM_DD_HH_MM_SS.getPattern()))
                    .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .failOnUnknownProperties(true)
                    .failOnEmptyBeans(false)
                    .locale(Locale.CHINA);
        };
    }

    /**
     * 使用配置方式
     */
    @Bean
    public FilterRegistrationBean<Filter> xssFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setAsyncSupported(true);
        filterRegistrationBean.addUrlPatterns("/*");// 这里是 /* ,不是 /**
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setName("xssFilter");
        filterRegistrationBean.setOrder(0);
        return filterRegistrationBean;
    }

    @Autowired
    private GlobalPropertyInterceptor.RequestPropertyProperties requestProperty;

    /**
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

//    /**
//     * @see #jackson2ObjectMapperBuilderCustomizer()
//     */
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.forEach(converter -> {
//            if (converter instanceof StringHttpMessageConverter) {
//                ((StringHttpMessageConverter) converter).setDefaultCharset(Contants.CHARSET_UTF_8);
//            } else if (converter instanceof MappingJackson2HttpMessageConverter) {
////                SimpleModule module = new SimpleModule();
////                module.addDeserializer(QueryPage.class,
////                        new QueryPageReferenceTypeDeserializer()));
//                MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converter;
////                mappingJackson2HttpMessageConverter.setObjectMapper();
//                mappingJackson2HttpMessageConverter.setDefaultCharset(Contants.CHARSET_UTF_8);
//            }
//        });
//    }

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

//    /**
//     * 添加方法参数解析
//     *
//     * @param resolvers resolvers
//     */
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new LoginUserHandlerMethodArgumentResolver());
//    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        boolean securityContextPresent = ClassUtils.isPresent("com.hk.core.authentication.api.SecurityContextUtils", null);
        if (securityContextPresent) {
            registry.addInterceptor(new UserContextInterceptor()).addPathPatterns("/**");
        }
        boolean accessTokenPresent = ClassUtils.isPresent("com.hk.core.authentication.oauth2.utils.AccessTokenUtils", null);
        if (accessTokenPresent) {
            registry.addInterceptor(new AccessTokenInterceptor()).addPathPatterns("/**");
        }
        GlobalPropertyInterceptor propertyInterceptor = new GlobalPropertyInterceptor();
        Map<String, Object> property = requestProperty.getProperty();
        if (CollectionUtils.isNotEmpty(property)) {
            propertyInterceptor.setProperty(property);
        }
        registry.addInterceptor(propertyInterceptor).addPathPatterns("/**");

        /* ****************** 国际化支持******************* */
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }

//    private class QueryPageReferenceTypeDeserializer extends ReferenceTypeDeserializer<QueryPage<?>> {
//
//
//        public QueryPageReferenceTypeDeserializer(JavaType fullType, ValueInstantiator vi,
//                                                  TypeDeserializer typeDeser, JsonDeserializer<?> deser) {
//            super(fullType, vi, typeDeser, deser);
//        }
//
//        @Override
//        protected ReferenceTypeDeserializer<QueryPage<?>> withResolved(TypeDeserializer typeDeser, JsonDeserializer<?> valueDeser) {
//            return new QueryPageReferenceTypeDeserializer(_fullType, _valueInstantiator, typeDeser, valueDeser);
//        }
//
//        @Override
//        public QueryPage<?> getNullValue(DeserializationContext ctxt) {
//            return new SimpleQueryPage<>();
//        }
//
//        @Override
//        public QueryPage<?> referenceValue(Object contents) {
//            SimpleQueryPage<Object> queryPage = new SimpleQueryPage<>();
//            queryPage.setData((List<Object>) contents);
//            return queryPage;
//        }
//
//        @Override
//        public QueryPage<?> updateReference(QueryPage<?> reference, Object contents) {
//            SimpleQueryPage<Object> queryPage = new SimpleQueryPage<>();
//            queryPage.setData((List<Object>) contents);
//            return queryPage;
//        }
//
//        @Override
//        public Object getReferenced(QueryPage<?> reference) {
//            return reference.getData();
//        }
//    }
}


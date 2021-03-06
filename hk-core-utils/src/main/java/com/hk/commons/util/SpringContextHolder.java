package com.hk.commons.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;

import java.util.Map;
import java.util.Optional;

/**
 * Spring Bean 工具类
 *
 * @author kevin
 * @date 2018-04-16 09:41
 */
@Slf4j
@Order(0)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 根据名称获取Bean
     *
     * @param name beanName
     * @return T
     * @throws BeansException {@link BeansException}
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

    public static boolean containsBean(String name) {
        return applicationContext != null && applicationContext.containsBean(name);
    }

    /**
     * 根据Bean类型获取bean
     *
     * @param clazz beanClass
     * @return T
     * @throws BeansException {@link BeansException}
     */
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return applicationContext == null ? null : applicationContext.getBean(clazz);
    }

    public static <T> Optional<T> getBeanIfExist(Class<T> clazz) {
        return Optional.ofNullable(applicationContext == null ? null : applicationContext.getBeanProvider(clazz).getIfAvailable());
    }


    /**
     * 根据类型获取 Bean
     *
     * @param clazz bean 类型
     * @param <T>   T
     * @return Map
     */
    public static <T> Map<String, T> getBeanOfType(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    /**
     * 根据名称获取 Bean
     *
     * @param name  name
     * @param clazz bean 类型
     * @param <T>   T
     * @return Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 获取国际化消息
     *
     * @param code code
     * @param args args
     * @return i18n Message
     */
    public static String getMessage(String code, Object... args) {
        return getMessageWithDefault(code, code, args);
    }

    /**
     * 获取国际化消息
     *
     * @param code           code
     * @param defaultMessage defaultMessage
     * @param args           args
     * @return i18n Message
     */
    public static String getMessageWithDefault(String code, String defaultMessage, Object... args) {
        return null == applicationContext ? code : applicationContext.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
    }

    /**
     * applicationContext destroy
     */
    @Override
    public void destroy() {
        if (log.isDebugEnabled()) {
            log.debug("applicationContext destroy......");
        }
        applicationContext = null;
    }


}

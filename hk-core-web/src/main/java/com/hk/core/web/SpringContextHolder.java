package com.hk.core.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring 工具类，获取Bean实例
 *
 * @author huangkai
 * @date 2017年9月29日下午2:02:05
 */
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	private static final Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 根据名称获取Bean
	 *
	 * @param name
	 * @return
	 * @throws BeansException
	 */
	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}

	/**
	 * 根据Bean类型获取bean
	 *
	 * @param clazz
	 * @return
	 * @throws BeansException
	 */
	public static <T> T getBean(Class<T> clazz) throws BeansException {
		return applicationContext.getBean(clazz);
	}

	@Override
	public void destroy() throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("applicationContext destroy......");
		}
		applicationContext = null;
	}

}

package com.hk.core.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Druid DataSource 自动配置
 * 
 * @author huangkai
 * @date 2017年9月27日下午3:42:07
 */
@Configuration
@ConditionalOnClass(value = DruidDataSource.class)
@ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource", matchIfMissing = true)
public class DruidDataSourceAutoConfiguration {

	/**
	 * 
	 * @param properties
	 * @param type
	 * @return
	 */
	protected DataSource createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
		return properties.initializeDataSourceBuilder().type(type).build();
	}

	/**
	 * DruidDataSource
	 * 
	 * @param properties
	 *            读入的配置
	 * @return
	 */
	@Bean
	@ConfigurationProperties("spring.datasource.druid")
	public DruidDataSource dataSource(DataSourceProperties properties) {
		DruidDataSource dataSource = (DruidDataSource) createDataSource(properties, DruidDataSource.class);
		String validationQuery = dataSource.getValidationQuery();
		if (null != validationQuery) {
			dataSource.setTestOnBorrow(true);
			dataSource.setValidationQuery(validationQuery);
		}
		return dataSource;
	}

}

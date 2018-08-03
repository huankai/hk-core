package com.hk.core.autoconfigure.data;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * Druid DataSource 自动配置
 *
 * @author: kevin
 * @date 2017年9月27日下午3:42:07
 */
@Configuration
@ConditionalOnClass(value = DruidDataSource.class)
@ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource", matchIfMissing = true)
public class DruidDataSourceAutoConfiguration {

    private DataSource createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
        return properties.initializeDataSourceBuilder().type(type).build();
    }

    /**
     * DruidDataSource
     *
     * @param properties 读入的配置
     * @return DruidDataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DruidDataSource dataSource(DataSourceProperties properties) {
        DruidDataSource dataSource = (DruidDataSource) createDataSource(properties, DruidDataSource.class);
        String validationQuery = dataSource.getValidationQuery();
        if (null != validationQuery) {
            dataSource.setTestOnBorrow(true);
            dataSource.setValidationQuery(validationQuery);
        }
        return dataSource;
    }

    @Configuration
    @ConditionalOnWebApplication
    static class DruidDataWebAutoConfiguration {
        /**
         * 注册一个StatViewServlet
         */
        @Bean
        public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
            ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(),
                    "/monitor/druid/*");
            // 添加初始化参数：initParams
            // 白名单：
            registrationBean.addInitParameter("allow", "127.0.0.1");
            // IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to
            // view this page.
            registrationBean.addInitParameter("deny", "192.168.1.73");
            // 登录查看信息的账号密码.
            registrationBean.addInitParameter("loginUsername", "root");
            registrationBean.addInitParameter("loginPassword", "password");
            // 是否能够重置数据.
            registrationBean.addInitParameter("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
            return registrationBean;
        }

        /**
         * 注册一个：filterRegistrationBean
         */
        @Bean
        public FilterRegistrationBean<WebStatFilter> druidStatFilter() {
            FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());
            filterRegistrationBean.setName("druidWebStatFilter");
            // 添加过滤规则.
            filterRegistrationBean.addUrlPatterns("/*");
            // 添加忽略的格式信息.
            filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/monitor/druid/*");
            return filterRegistrationBean;
        }
    }

}

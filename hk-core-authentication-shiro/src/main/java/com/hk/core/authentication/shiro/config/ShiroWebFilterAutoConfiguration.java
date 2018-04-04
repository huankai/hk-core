package com.hk.core.authentication.shiro.config;

import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hk.core.authentication.shiro.filters.AjaxFormAuthenticationFilter;

/**
 * 
 * @author huangkai
 * @date 2017年10月24日下午3:25:54
 */
@Configuration
public class ShiroWebFilterAutoConfiguration extends ShiroWebFilterConfiguration {

	@Bean
	@Override
	protected ShiroFilterFactoryBean shiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = super.shiroFilterFactoryBean();
		shiroFilterFactoryBean.setLoginUrl(loginUrl);
		shiroFilterFactoryBean.setSuccessUrl(successUrl);
		shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);

		shiroFilterFactoryBean.setSecurityManager(securityManager);
		
		Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
		AjaxFormAuthenticationFilter ajaxFormAuthenticationFilter = new AjaxFormAuthenticationFilter();
		ajaxFormAuthenticationFilter.setSaveRequestedUrlEnabled(false);
		filters.put("authc", ajaxFormAuthenticationFilter); // 认证过滤

		filters.put("logout", new LogoutFilter()); // 登出过滤

		Map<String, String> filterChainMap = shiroFilterChainDefinition.getFilterChainMap();
//		filterChainMap.put("/login", "authc");
//		filterChainMap.put("/callback", "callback");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
		return shiroFilterFactoryBean;
	}
}

package com.hk.core.authentication.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.hk.core.authentication.security.DefaultUserDetailServiceImpl;
import com.hk.core.authentication.security.SpringSecurityContext;
import com.hk.core.authentication.security.encrypt.SecurityPasswordEncrypt;

@Configuration
public class SecurityAuthenticationAutoConfiguration {

	/**
	 * 默认的认证
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(value = { UserDetailsService.class })
	public UserDetailsService userDetails() {
		return new DefaultUserDetailServiceImpl();
	}

	/**
	 * 密码加密方式
	 * 
	 * @return
	 */
	@Bean
	public SecurityPasswordEncrypt passwordencrypt() {
		return new SecurityPasswordEncrypt();
	}

	@Bean
	public SpringSecurityContext securityContext() {
		return new SpringSecurityContext();
	}

}

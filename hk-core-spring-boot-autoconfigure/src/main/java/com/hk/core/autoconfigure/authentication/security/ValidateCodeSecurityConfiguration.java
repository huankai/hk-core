package com.hk.core.autoconfigure.authentication.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.Filter;

/**
 * 验证码配置
 *
 * @author: kevin
 * @date 2018-07-27 11:20
 */
public class ValidateCodeSecurityConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final Filter validateCodeFilter;

    public ValidateCodeSecurityConfiguration(Filter validateCodeFilter) {
        this.validateCodeFilter = validateCodeFilter;
    }

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(validateCodeFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }
}

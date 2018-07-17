package com.hk.core.autoconfigure.authentication.security;

import com.hk.core.authentication.security.SpringSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置
 *
 * @author: kevin
 * @date 2017年12月18日下午5:26:46
 */
@Order(5)
@Configuration
@EnableWebSecurity
@ConditionalOnClass(value = {SpringSecurityContext.class})
@AutoConfigureAfter(SecurityAuthenticationAutoConfiguration.class)
public class SecurityWebAutoConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 用户Service
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /* CSRF Disable*/
                .csrf().disable()
                .anonymous().disable();

    }

    /**
     * 不定义没有password grant_type
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

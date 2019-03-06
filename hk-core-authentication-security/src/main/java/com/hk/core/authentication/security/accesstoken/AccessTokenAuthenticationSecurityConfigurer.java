package com.hk.core.authentication.security.accesstoken;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author huangkai
 * @date 2019/3/5 16:11
 */
public class AccessTokenAuthenticationSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AccessTokenContext accessTokenContext;

    private final String header;

    private final String tokenParameter;

    public AccessTokenAuthenticationSecurityConfigurer(AccessTokenContext accessTokenContext, String header, String tokenParameter) {
        this.accessTokenContext = accessTokenContext;
        this.header = header;
        this.tokenParameter = tokenParameter;
    }

    @Override
    public void configure(HttpSecurity builder) {
        AccessTokenAuthenticationFilter filter = new AccessTokenAuthenticationFilter(header, tokenParameter);
        filter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        AccessTokenAuthenticationProvider provider = new AccessTokenAuthenticationProvider(accessTokenContext);
        builder.authenticationProvider(provider).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}

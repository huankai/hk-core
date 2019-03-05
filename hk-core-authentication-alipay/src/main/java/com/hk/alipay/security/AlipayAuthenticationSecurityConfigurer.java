package com.hk.alipay.security;

import com.alipay.api.AlipayClient;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author huangkai
 * @date 2019/3/5 18:18
 */
public class AlipayAuthenticationSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private AlipayClient alipayClient;

    private String processesUrl;

    public AlipayAuthenticationSecurityConfigurer(AlipayClient alipayClient, String processesUrl) {
        this.alipayClient = alipayClient;
        this.processesUrl = processesUrl;
    }

    @Override
    public void configure(HttpSecurity builder) {
        AlipayCallbackAuthenticationFilter filter = new AlipayCallbackAuthenticationFilter(processesUrl, alipayClient);
        filter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        AlipayAuthenticationProvider provider = new AlipayAuthenticationProvider();
        builder.authenticationProvider(provider).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}

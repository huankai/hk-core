package com.hk.alipay.security;

import com.alipay.api.AlipayClient;
import com.hk.alipay.AlipayProperties;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 支付宝认证配置
 *
 * @author huangkai
 * @date 2019/3/5 18:18
 */
@AllArgsConstructor
public class AlipayAuthenticationSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private AlipayClient alipayClient;

    private String processesUrl;

    private AlipayProperties properties;

    @Override
    public void configure(HttpSecurity builder) {
        AlipayCallbackAuthenticationFilter filter = new AlipayCallbackAuthenticationFilter(processesUrl, alipayClient, properties);
        filter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        AlipayAuthenticationProvider provider = new AlipayAuthenticationProvider();
        builder.authenticationProvider(provider).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}

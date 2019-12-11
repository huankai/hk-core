package com.hk.core.autoconfigure.authentication.security;

import com.alipay.api.AlipayClient;
import com.hk.alipay.security.AlipayAuthenticationProvider;
import com.hk.alipay.security.AlipayCallbackAuthenticationFilter;
import com.hk.core.authentication.api.PostAuthenticationHandler;
import com.hk.core.authentication.api.UserPrincipal;
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

    private String state;

    private String scope;

    private PostAuthenticationHandler<UserPrincipal, String> authenticationHandler;

    @Override
    public void configure(HttpSecurity builder) {
        var filter = new AlipayCallbackAuthenticationFilter(processesUrl, alipayClient,
                state, scope);
        filter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        var provider = new AlipayAuthenticationProvider(authenticationHandler);
        builder.authenticationProvider(provider).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}

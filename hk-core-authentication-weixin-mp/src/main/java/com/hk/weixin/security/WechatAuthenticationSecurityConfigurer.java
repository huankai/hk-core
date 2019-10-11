package com.hk.weixin.security;


import com.hk.weixin.WechatMpProperties;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 微信认证配置
 *
 * @author kevin
 * @date 2018年2月8日上午11:38:35
 */
public class WechatAuthenticationSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final WxMpService wxMpService;

    private final WechatMpProperties.Authentication authentication;

    public WechatAuthenticationSecurityConfigurer(WxMpService wxMpService, WechatMpProperties.Authentication authentication) {
        this.wxMpService = wxMpService;
        this.authentication = authentication;
    }

    /**
     * 此方法是将 WechatAuthenticationProvider 注册到spring security的filter 中
     */
    @Override
    public void configure(HttpSecurity http) {
        WechatCallbackAuthenticationFilter filter = new WechatCallbackAuthenticationFilter(wxMpService, authentication);
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        WechatAuthenticationProvider provider = new WechatAuthenticationProvider();
        http.authenticationProvider(provider).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}

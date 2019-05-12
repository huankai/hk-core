package com.hk.weixin.security;


import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.api.UserPrincipalService;
import com.hk.weixin.WechatMpProperties;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class WechatAuthenticationSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final WxMpService wxMpService;

    private final WechatMpProperties.Authentication authentication;

    private final UserPrincipalService<UserPrincipal,UserPrincipal> wechatUserPrincipalService;

    /**
     * 此方法是将 WechatAuthenticationProvider 注册到spring security的filter 中
     */
    @Override
    public void configure(HttpSecurity http) {
        WechatCallbackAuthenticationFilter filter = new WechatCallbackAuthenticationFilter(wxMpService, authentication);
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        WechatAuthenticationProvider provider = new WechatAuthenticationProvider(wechatUserPrincipalService);
        http.authenticationProvider(provider).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}

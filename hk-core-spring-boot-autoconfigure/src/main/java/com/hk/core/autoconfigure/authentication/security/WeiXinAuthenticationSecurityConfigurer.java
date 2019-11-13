package com.hk.core.autoconfigure.authentication.security;


import com.hk.core.authentication.api.PostAuthenticationHandler;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.autoconfigure.weixin.WeiXinMpProperties;
import com.hk.weixin.security.WeiXinAuthenticationProvider;
import com.hk.weixin.security.WeiXinCallbackAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
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
public class WeiXinAuthenticationSecurityConfigurer
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final WxMpService wxMpService;

    private final WeiXinMpProperties.Authentication authentication;

    private final PostAuthenticationHandler<UserPrincipal, WxMpUser> authenticaionHandler;

    /**
     * 此方法是将 WechatAuthenticationProvider 注册到spring security的filter 中
     */
    @Override
    public void configure(HttpSecurity http) {
        WeiXinCallbackAuthenticationFilter filter = new WeiXinCallbackAuthenticationFilter(wxMpService, authentication.getCallbackUrl(),
                authentication.getState());
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        WeiXinAuthenticationProvider provider = new WeiXinAuthenticationProvider(authenticaionHandler);
        http.authenticationProvider(provider).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}

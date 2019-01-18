/**
 *
 */
package com.hk.core.autoconfigure.weixin.authentication.qrcode;


import com.hk.weixin.qrcode.WechatQrCodeProperties;
import com.hk.weixin.qrcode.security.WechatQrCodeAuthenticationProvider;
import com.hk.weixin.qrcode.security.WechatQrCodeCallbackAuthenticationFilter;

import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 微信二维码配置
 *
 * @author kevin
 * @date 2018年2月8日上午11:38:35
 */
public class WechatQrcodeAuthenticationSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private WxMpService wxMpService;

    private WechatQrCodeProperties qrCodeProperties;

    public WechatQrcodeAuthenticationSecurityConfigurer(WxMpService wxMpService, WechatQrCodeProperties qrCodeProperties) {
        this.wxMpService = wxMpService;
        this.qrCodeProperties = qrCodeProperties;
    }

    /**
     * 此方法是将 WechatQrCodeAuthenticationProvider 注册到spring security的filter 中
     */
    @Override
    public void configure(HttpSecurity http) {
        WechatQrCodeCallbackAuthenticationFilter filter = new WechatQrCodeCallbackAuthenticationFilter(wxMpService, qrCodeProperties);
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        WechatQrCodeAuthenticationProvider provider = new WechatQrCodeAuthenticationProvider();
        http.authenticationProvider(provider).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}

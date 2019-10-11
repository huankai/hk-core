package com.hk.weixin.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kevin
 * @date 2019-7-18 18:01
 */
public class WechatAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WechatAuthenticationDetails> {

    @Override
    public WechatAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new WechatAuthenticationDetails(context);
    }
}

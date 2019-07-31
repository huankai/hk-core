package com.hk.alipay.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kevin
 * @date 2019-7-18 17:57
 */
public class AlipayAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, AlipayAuthenticationDetails> {

    @Override
    public AlipayAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new AlipayAuthenticationDetails(context);
    }
}

package com.hk.weixin.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kevin
 * @date 2019-7-18 18:01
 */
public class WechatAuthenticationDetails extends WebAuthenticationDetails {

    public WechatAuthenticationDetails(HttpServletRequest request) {
        super(request);
    }
}

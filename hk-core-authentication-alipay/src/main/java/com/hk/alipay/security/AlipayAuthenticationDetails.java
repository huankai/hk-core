package com.hk.alipay.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kevin
 * @date 2019-7-18 17:58
 */
public class AlipayAuthenticationDetails extends WebAuthenticationDetails {

    public AlipayAuthenticationDetails(HttpServletRequest request) {
        super(request);
    }
}

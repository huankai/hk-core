package com.hk.core.autoconfigure.alipay;

import com.hk.core.web.ServletContextHolder;

/**
 * 支付宝地址常量
 *
 * @author huangkai
 * @date 2019/3/5 17:37
 */
public interface AlipayConstants {

    /**
     * OAUTH2地址 正式环境
     */
    String OAUTH2 = "https://openauth.alipay.com/oauth2/";

    /**
     * OAUTH2地址 沙箱模式
     */
    String OAUTH2_DEV = "https://openauth.alipaydev.com/oauth2/";

    /**
     * 网关地址 正式环境
     */
    String GATEWAY = "https://openapi.alipay.com/gateway.do";

    /**
     * 网关地址 沙箱模式
     */
    String GATEWAY_DEV = "https://openapi.alipaydev.com/gateway.do";

    /**
     * 获取OAUTH2地址
     *
     * @param dev 是否为沙箱模式
     * @return OAUTH2地址
     */
    static String oauth2(boolean dev) {
        return dev ? OAUTH2_DEV : OAUTH2;
    }

    /**
     * 获取 支付宝认证跳转 url
     *
     * @param properties properties
     * @return authorize Url
     * @see https://docs.open.alipay.com/289/105656
     */
    static String getPublicAppAuthorizeUrl(AlipayProperties properties) {
        String redirectUri = String.format("%s%s%s", properties.getCallHost(), ServletContextHolder.getContextPath(), properties.getCallbackUrl());
        return String.format("%spublicAppAuthorize.htm?app_id=%s&scope=%s&redirect_uri=%s",
                oauth2(properties.isDev()), properties.getAppId(), properties.getScope(),
                redirectUri);
    }

    /**
     * 获取网关地址
     *
     * @param dev 是否为沙箱模式
     * @return 网关地址
     */
    static String gateway(boolean dev) {
        return dev ? GATEWAY_DEV : GATEWAY;
    }
}

package com.hk.core.autoconfigure.weixin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kevin
 * @date 2019-12-7 16:27
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WeiXinPayProperties {

    /**
     * 是否开启支付
     */
    private boolean enabled;

    /**
     * 微信 appId,需要微信支付平台关联微信公众平台后的公众平台 id
     */
    private String appId;

    /**
     * 商户 id
     */
    private String mchId;

    /**
     * 商户 Key
     */
    private String mchKey;

    /**
     * 沙箱环境 商户 Key
     */
    private String mchKeySandbox;

    /**
     * 证书路径
     */
    private String keyPath;

    /**
     * 支付成功后的通知回调地址
     */
    private String payNotifyUrl;

    /**
     * 退款成功后的通知回调地址
     */
    private String refundNotifyUrl;

    /**
     * 是否使用沙箱环境
     */
    private boolean useSandboxEnv;

    /**
     * 交易类型.
     * <pre>
     * JSAPI--公众号支付
     * NATIVE--原生扫码支付
     * APP--app支付
     * </pre>
     */
    private String tradeType;
}

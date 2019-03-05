package com.hk.alipay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huangkai
 * @date 2019/3/5 17:35
 */
@Data
@ConfigurationProperties(prefix = "alipay")
public class AlipayProperties {

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 是否沙箱模式
     */
    private boolean dev;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 参数返回格式，目前只支持json
     */
    private AlipayFormat format = AlipayFormat.json;

    /**
     * 支付宝公钥
     */
    private String publicKey;

    /**
     * 签名模式,目前支持RSA2和RSA，推荐使用RSA2
     */
    private AlipaySignType signType = AlipaySignType.RSA2;

    /**
     * 回调地址
     */
    private String callbackUrl = "/alipay/callback";

    /**
     * 参数返回格式
     *
     * @see https://docs.open.alipay.com/218/105326/
     */
    public enum AlipayFormat {

        json
    }

    /**
     * 签名模式
     *
     * @see https://docs.open.alipay.com/218/105326/
     */
    public enum AlipaySignType {

        RSA,

        RSA2
    }
}

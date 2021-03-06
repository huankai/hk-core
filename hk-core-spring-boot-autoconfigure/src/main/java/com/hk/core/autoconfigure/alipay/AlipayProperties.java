package com.hk.core.autoconfigure.alipay;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author huangkai
 * @date 2019/3/5 17:35
 */
@Getter
@Setter
@RefreshScope
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
     * 一个随机字符串，支付宝调用回调地址时会返回
     */
    private String state;

    /**
     * 私钥，此值是 支付宝文档中下载的 secret_key_tools_RSA_win 包中的 RSA签名验签工具.bat 文件生成的 应用私钥2048.txt 文件内容值。
     */
    private String privateKey;

    /**
     * 参数返回格式，目前只支持json
     */
    private AlipayFormat format = AlipayFormat.json;

    /**
     * <pre>
     *
     * 支付宝公钥，在应用信息 -> 开发设置 -> 加签方式 ->  查看支付宝公钥
     *
     * 需要先在支付宝文档中下载 secret_key_tools_RSA256_win 包后执行 支付宝RAS密钥生成器SHAwithRSA2048_V1.0.bat 文件，将生成的 RSA 公钥上传到
     *  应用信息 -> 开发设置 -> 加签方式 ->  查看支付宝公钥 中。
     * </pre>
     */
    private String alipayPublicKey;

    /**
     * 签名模式,目前支持RSA2和RSA，推荐使用RSA2
     */
    private AlipaySignType signType = AlipaySignType.RSA2;

    /**
     * 支付宝授权回调的 host，支付宝授权回调的 地址为 callHost + callbackUrl
     */
    private String callHost;

    /**
     * 回调地址支付宝授权回调的 url
     */
    private String callbackUrl = "/alipay/callback";

    /**
     * <pre>
     * auth_user（获取用户信息、网站支付宝登录）、
     * auth_base（用户信息授权）、
     * auth_ecard（商户会员卡）、
     * auth_invoice_info（支付宝闪电开票）、
     * auth_puc_charge（生活缴费）五个值;
     * 多个scope时用”,”分隔，如scope为”auth_user,auth_ecard”时
     * </pre>
     */
    private String scope = "auth_base";

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

    /* ****************************** 支付相关参数 **************************** */

    /**
     * 支付成功后的通知地址，支付宝会发送 POST　请求到此地址
     */
    private String notifyUrl;

    /**
     * 支买家付款成功后,会跳到 return_url 所在的页面,这个页面可以展示给客户看,这个页面只有付款成功才会跳转,并且只跳转一次
     */
    private String returnUrl;
}

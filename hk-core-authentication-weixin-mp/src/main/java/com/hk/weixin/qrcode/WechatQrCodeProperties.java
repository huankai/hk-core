package com.hk.weixin.qrcode;

import com.hk.commons.util.StringUtils;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * 微信二维码扫码参数
 *
 * @author kevin
 * @date 2018年2月8日下午3:50:15
 */
@Data
@ConfigurationProperties(prefix = "wechat.qrcode")
public class WechatQrCodeProperties {

    /**
     * 二维码扫码回调
     */
    @NotNull
    private String callbackUrl;

    private String callHost;

    /**
     * 一个随机的字符串，非必填，微信扫码回调的时候会带回此参数，此参数可用于防止csrf攻击（跨站请求伪造攻击）
     */
    private String state;

    /**
     * @param callbackUrl the callbackUrl to set
     */
    public void setCallbackUrl(String callbackUrl) {
        if (!StringUtils.startsWith(callbackUrl, "/")) {
            callbackUrl = "/" + callbackUrl;
        }
        this.callbackUrl = callbackUrl;
    }
}

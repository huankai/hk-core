package com.hk.weixin.qrcode;

import com.hk.commons.util.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

/**
 * 微信二维码扫码参数
 *
 * @author: kevin
 * @date 2018年2月8日下午3:50:15
 */
@Configuration
@ConfigurationProperties(prefix = "wechat.qrcode")
public class WechatQrCodeConfig {

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
     * @return the callbackUrl
     */
    public String getCallbackUrl() {
        return callbackUrl;
    }

    /**
     * @param callbackUrl the callbackUrl to set
     */
    public void setCallbackUrl(String callbackUrl) {
        if (!StringUtils.startsWith(callbackUrl, "/")) {
            callbackUrl = "/" + callbackUrl;
        }
        this.callbackUrl = callbackUrl;
    }

    /**
     * @return the callHost
     */
    public String getCallHost() {
        return callHost;
    }

    /**
     * @param callHost the callHost to set
     */
    public void setCallHost(String callHost) {
        this.callHost = callHost;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

}

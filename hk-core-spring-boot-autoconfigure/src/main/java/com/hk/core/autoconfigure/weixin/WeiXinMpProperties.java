package com.hk.core.autoconfigure.weixin;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import javax.validation.constraints.NotNull;

/**
 * wechat mp properties
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "wx.mp")
public class WeiXinMpProperties {

    private boolean enabled;
    /**
     * 设置微信公众号的appid
     */
    private String appId;

    /**
     * 设置微信公众号的app secret
     */
    private String secret;

    /**
     * 设置微信公众号的token
     */
    private String token;

    /**
     * 设置微信公众号的EncodingAESKey
     */
    private String aesKey;

    @NestedConfigurationProperty
    private Authentication authentication = new Authentication();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Data
    public static class Authentication {

        /**
         * 微信登陆回调
         */
        @NotNull
        private String callbackUrl;

        private String callHost;

        /**
         * 一个随机的字符串，非必填，微信扫码回调的时候会带回此参数，此参数可用于防止csrf攻击（跨站请求伪造攻击）
         */
        private String state;

        /**
         * <pre>
         *     snsapi_base: 静默授权，用户无感知，只能获取用户的 openid
         *     snsapi_userinfo: 非静默授权，需要用户同意授权，可获取用户更多信息
         * </pre>
         */
        private String scope = "snsapi_base";
    }
}

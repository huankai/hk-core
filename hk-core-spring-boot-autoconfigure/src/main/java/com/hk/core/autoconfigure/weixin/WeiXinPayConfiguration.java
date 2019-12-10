package com.hk.core.autoconfigure.weixin;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.hk.commons.util.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kevin
 * @date 2019-12-7 16:27
 */
@Configuration
@ConditionalOnClass({WxPayService.class})
@ConditionalOnProperty(prefix = "wx.pay", name = "enabled", havingValue = "true")
@EnableConfigurationProperties({WeiXinPayProperties.class})
public class WeiXinPayConfiguration {

    private final WeiXinPayProperties weiXinPayProperties;

    public WeiXinPayConfiguration(WeiXinPayProperties weiXinPayProperties) {
        this.weiXinPayProperties = weiXinPayProperties;
    }

    @Bean
    @ConditionalOnMissingBean(WxPayService.class)
    public WxPayService wxPayService() {
        var wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(weiXinPayProperties.getAppId());
        wxPayConfig.setMchId(weiXinPayProperties.getMchId());
        wxPayConfig.setMchKey(weiXinPayProperties.getMchKey());
        if (weiXinPayProperties.isUseSandboxEnv()) {
            wxPayConfig.setMchKey(weiXinPayProperties.getMchKeySandbox());
            wxPayConfig.setUseSandboxEnv(true);
        }
        wxPayConfig.setTradeType(StringUtils.defaultIfEmpty(weiXinPayProperties.getTradeType(), WxPayConstants.TradeType.JSAPI));
        wxPayConfig.setNotifyUrl(weiXinPayProperties.getPayNotifyUrl());
        wxPayConfig.setKeyPath(weiXinPayProperties.getKeyPath());
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig);
        return wxPayService;
    }
}

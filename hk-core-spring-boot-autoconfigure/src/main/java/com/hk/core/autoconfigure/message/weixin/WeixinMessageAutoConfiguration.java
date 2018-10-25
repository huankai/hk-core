package com.hk.core.autoconfigure.message.weixin;

import com.hk.core.autoconfigure.weixin.WechatMpConfiguration;
import com.hk.message.weixin.WeixinTemplateMessager;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: huangkai
 * @date: 2018-9-23 14:47
 */
@Configuration
@ConditionalOnClass(WeixinTemplateMessager.class)
@AutoConfigureAfter(WechatMpConfiguration.class)
public class WeixinMessageAutoConfiguration {

    private final WxMpService wxMpService;

    public WeixinMessageAutoConfiguration(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    @Bean("weixinTemplateMessager")
    public WeixinTemplateMessager WeixinTemplateMessager() {
        return new WeixinTemplateMessager(wxMpService);
    }
}

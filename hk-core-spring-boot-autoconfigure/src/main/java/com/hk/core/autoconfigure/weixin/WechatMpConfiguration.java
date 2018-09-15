package com.hk.core.autoconfigure.weixin;

import com.hk.commons.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;


/**
 * wechat mp configuration
 */
@Configuration
@ConditionalOnClass(WxMpService.class)
@Conditional(WechatMpConfiguration.WeixinMpCondition.class)
@EnableConfigurationProperties(WechatMpProperties.class)
public class WechatMpConfiguration {

    private WechatMpProperties properties;

    public WechatMpConfiguration(WechatMpProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMpConfigStorage configStorage() {
        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(this.properties.getAppId());
        configStorage.setSecret(this.properties.getSecret());
        configStorage.setToken(this.properties.getToken());
        configStorage.setAesKey(this.properties.getAesKey());
        return configStorage;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMpService wxMpService(WxMpConfigStorage configStorage) {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(configStorage);
        return wxMpService;
    }

    static class WeixinMpCondition extends SpringBootCondition {

        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ConditionMessage.Builder message = ConditionMessage.forCondition("Weixin MP Condition", new Object[0]);
            Environment environment = context.getEnvironment();
            String appId = environment.getProperty("wechat.mp.app-id");
            return StringUtils.isEmpty(appId) ?
                    ConditionOutcome.noMatch(message.didNotFind("wechat mp appId").atAll())
                    : ConditionOutcome.match();
        }
    }

}
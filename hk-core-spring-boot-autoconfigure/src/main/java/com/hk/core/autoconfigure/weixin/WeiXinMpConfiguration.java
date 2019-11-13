package com.hk.core.autoconfigure.weixin;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * wechat mp configuration
 */
@Configuration
@ConditionalOnClass(WxMpService.class)
//@Conditional(WeiXinMpConfiguration.WeixinMpCondition.class)
@ConditionalOnProperty(prefix = "wechat.mp", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(WeiXinMpProperties.class)
public class WeiXinMpConfiguration {

    private WeiXinMpProperties properties;

    public WeiXinMpConfiguration(WeiXinMpProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMpDefaultConfigImpl configStorage() {
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
        configStorage.setAppId(properties.getAppId());
        configStorage.setSecret(properties.getSecret());
        configStorage.setToken(properties.getToken());
        configStorage.setAesKey(properties.getAesKey());
        return configStorage;
    }

    @Bean
    public WxMpService wxMpService(WxMpConfigStorage configStorage) {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(configStorage);
        return wxMpService;
    }

//    static class WeixinMpCondition extends SpringBootCondition {
//
//        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
//            ConditionMessage.Builder message = ConditionMessage.forCondition("Weixin MP Condition");
//            Environment environment = context.getEnvironment();
//            String appId = environment.getProperty("wechat.mp.app-id");
//            return StringUtils.isEmpty(appId) ?
//                    ConditionOutcome.noMatch(message.didNotFind("wechat mp appId").atAll())
//                    : ConditionOutcome.match();
//        }
//    }

}

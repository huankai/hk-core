package com.hk.core.autoconfigure.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.hk.commons.util.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝 自动配置
 *
 * @author huangkai
 * @date 2019/3/5 22:13
 */
@Configuration
@ConditionalOnClass(AlipayClient.class)
@ConditionalOnProperty(prefix = "alipay", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(value = {AlipayProperties.class})
public class AlipayAutoConfiguration {

    private AlipayProperties alipayProperties;

    public AlipayAutoConfiguration(AlipayProperties alipayProperties) {
        this.alipayProperties = alipayProperties;
    }

    @Bean
    public AlipayClient alipayClient() {
        String gatewayUrl = AlipayConstants.gateway(alipayProperties.isDev());
        return new DefaultAlipayClient(gatewayUrl, alipayProperties.getAppId(),
                alipayProperties.getPrivateKey(), alipayProperties.getFormat().name(), Constants.UTF_8,
                alipayProperties.getAlipayPublicKey(), alipayProperties.getSignType().name());
    }
}

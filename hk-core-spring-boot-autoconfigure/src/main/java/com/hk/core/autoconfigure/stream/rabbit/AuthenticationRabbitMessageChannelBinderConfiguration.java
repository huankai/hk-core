package com.hk.core.autoconfigure.stream.rabbit;

import com.hk.authentication.stream.binder.rabbit.AuthenticationRabbitMessageChannelBinder;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.support.postprocessor.DelegatingDecompressingPostProcessor;
import org.springframework.amqp.support.postprocessor.GZipPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.binder.rabbit.config.RabbitMessageChannelBinderConfiguration;
import org.springframework.cloud.stream.binder.rabbit.properties.RabbitBinderConfigurationProperties;
import org.springframework.cloud.stream.binder.rabbit.properties.RabbitExtendedBindingProperties;
import org.springframework.cloud.stream.binder.rabbit.provisioning.RabbitExchangeQueueProvisioner;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

/**
 * 带有认证的 rabbit mq 消息发送，使用 spring security 实现
 *
 * @author huangkai
 * @date 2019-4-15 11:40
 * @see RabbitMessageChannelBinderConfiguration
 */
@Configuration
@ConditionalOnClass(value = {AuthenticationRabbitMessageChannelBinder.class})
@EnableConfigurationProperties(value = {RabbitBinderConfigurationProperties.class, RabbitExtendedBindingProperties.class})
public class AuthenticationRabbitMessageChannelBinderConfiguration {

    @Autowired
    private RabbitProperties rabbitProperties;
    @Autowired
    private ConnectionFactory rabbitConnectionFactory;

    @Autowired
    private RabbitBinderConfigurationProperties rabbitBinderConfigurationProperties;

    @Autowired
    private RabbitExtendedBindingProperties rabbitExtendedBindingProperties;

    @Bean
    MessagePostProcessor gZipPostProcessor() {
        GZipPostProcessor gZipPostProcessor = new GZipPostProcessor();
        gZipPostProcessor.setLevel(this.rabbitBinderConfigurationProperties.getCompressionLevel());
        return gZipPostProcessor;
    }

    @Bean
    MessagePostProcessor deCompressingPostProcessor() {
        return new DelegatingDecompressingPostProcessor();
    }

    /**
     * <pre>
     *
     * 重新配置带有认证的 RabbitMessageChannelBinder ，
     * 原配置查看 {@link RabbitMessageChannelBinderConfiguration#rabbitMessageChannelBinder(ListenerContainerCustomizer)}
     * </pre>
     *
     * @param listenerContainerCustomizer listenerContainerCustomizer
     * @return {@link AuthenticationRabbitMessageChannelBinder}
     */
    @Bean
    public AuthenticationRabbitMessageChannelBinder rabbitMessageChannelBinder(
            @Nullable ListenerContainerCustomizer<AbstractMessageListenerContainer> listenerContainerCustomizer) {
        AuthenticationRabbitMessageChannelBinder binder = new AuthenticationRabbitMessageChannelBinder(this.rabbitConnectionFactory,
                this.rabbitProperties, provisioningProvider(), listenerContainerCustomizer);
        binder.setAdminAddresses(this.rabbitBinderConfigurationProperties.getAdminAddresses());
        binder.setCompressingPostProcessor(gZipPostProcessor());
        binder.setDecompressingPostProcessor(deCompressingPostProcessor());
        binder.setNodes(this.rabbitBinderConfigurationProperties.getNodes());
        binder.setExtendedBindingProperties(this.rabbitExtendedBindingProperties);
        return binder;
    }

    @Bean
    RabbitExchangeQueueProvisioner provisioningProvider() {
        return new RabbitExchangeQueueProvisioner(this.rabbitConnectionFactory);
    }


}

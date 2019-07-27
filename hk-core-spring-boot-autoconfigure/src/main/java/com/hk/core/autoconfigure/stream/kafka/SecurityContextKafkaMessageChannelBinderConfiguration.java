package com.hk.core.autoconfigure.stream.kafka;

import com.hk.authentication.stream.binder.kafka.AuthenticationKafkaMessageChannelBinder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.binder.kafka.KafkaBindingRebalanceListener;
import org.springframework.cloud.stream.binder.kafka.properties.KafkaBinderConfigurationProperties;
import org.springframework.cloud.stream.binder.kafka.properties.KafkaExtendedBindingProperties;
import org.springframework.cloud.stream.binder.kafka.provisioning.KafkaTopicProvisioner;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.lang.Nullable;

/**
 * 带有认证的 kafka 消息发送，使用 spring security 实现
 *
 * @author huangkai
 * @date 2019-4-15 11:40
 * @see org.springframework.cloud.stream.binder.kafka.config.KafkaBinderConfiguration
 */
@Configuration
@ConditionalOnClass(value = {AuthenticationKafkaMessageChannelBinder.class, KafkaExtendedBindingProperties.class})
@Import(value = {KafkaAutoConfiguration.class})
@EnableConfigurationProperties({KafkaExtendedBindingProperties.class})
public class SecurityContextKafkaMessageChannelBinderConfiguration {

    @Autowired
    private KafkaExtendedBindingProperties kafkaExtendedBindingProperties;

    @Autowired
    private ProducerListener producerListener;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public AuthenticationKafkaMessageChannelBinder kafkaMessageChannelBinder(KafkaBinderConfigurationProperties configurationProperties,
                                                                             KafkaTopicProvisioner provisioningProvider,
                                                                             @Nullable ListenerContainerCustomizer<AbstractMessageListenerContainer<?, ?>> listenerContainerCustomizer,
                                                                             ObjectProvider<KafkaBindingRebalanceListener> rebalanceListener) {
        AuthenticationKafkaMessageChannelBinder kafkaMessageChannelBinder = new AuthenticationKafkaMessageChannelBinder(
                configurationProperties, provisioningProvider, listenerContainerCustomizer,
                rebalanceListener.getIfUnique());
        kafkaMessageChannelBinder.setProducerListener(producerListener);
        kafkaMessageChannelBinder.setExtendedBindingProperties(this.kafkaExtendedBindingProperties);
        return kafkaMessageChannelBinder;
    }

    @Bean
    KafkaBinderConfigurationProperties configurationProperties() {
        return new KafkaBinderConfigurationProperties(kafkaProperties);
    }

    @Bean
    KafkaTopicProvisioner provisioningProvider(KafkaBinderConfigurationProperties configurationProperties) {
        return new KafkaTopicProvisioner(configurationProperties, this.kafkaProperties);
    }

    @Bean
    @ConditionalOnMissingBean(ProducerListener.class)
    ProducerListener producerListener() {
        return new LoggingProducerListener();
    }


}

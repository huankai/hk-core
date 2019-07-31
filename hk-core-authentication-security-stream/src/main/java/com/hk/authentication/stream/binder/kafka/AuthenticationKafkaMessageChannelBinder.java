package com.hk.authentication.stream.binder.kafka;

import com.hk.authentication.interceptors.AuthenticationChannelInterceptor;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.binder.kafka.KafkaBindingRebalanceListener;
import org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder;
import org.springframework.cloud.stream.binder.kafka.properties.KafkaBinderConfigurationProperties;
import org.springframework.cloud.stream.binder.kafka.properties.KafkaProducerProperties;
import org.springframework.cloud.stream.binder.kafka.provisioning.KafkaTopicProvisioner;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.messaging.MessageChannel;

/**
 * rabbit
 *
 * @author kevin
 * @date 2019-4-24 15:39
 */
@Deprecated
public class AuthenticationKafkaMessageChannelBinder extends KafkaMessageChannelBinder {

    public AuthenticationKafkaMessageChannelBinder(KafkaBinderConfigurationProperties configurationProperties, KafkaTopicProvisioner provisioningProvider) {
        super(configurationProperties, provisioningProvider);
    }

    public AuthenticationKafkaMessageChannelBinder(KafkaBinderConfigurationProperties configurationProperties, KafkaTopicProvisioner provisioningProvider, ListenerContainerCustomizer<AbstractMessageListenerContainer<?, ?>> containerCustomizer, KafkaBindingRebalanceListener rebalanceListener) {
        super(configurationProperties, provisioningProvider, containerCustomizer, rebalanceListener);
    }

    @Override
    protected void postProcessOutputChannel(MessageChannel outputChannel, ExtendedProducerProperties<KafkaProducerProperties> producerProperties) {
        super.postProcessOutputChannel(outputChannel, producerProperties);
        ((AbstractMessageChannel) outputChannel).addInterceptor(new AuthenticationChannelInterceptor());
    }
}

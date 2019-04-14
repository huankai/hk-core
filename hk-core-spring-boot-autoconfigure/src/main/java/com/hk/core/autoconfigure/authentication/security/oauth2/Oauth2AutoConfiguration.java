package com.hk.core.autoconfigure.authentication.security.oauth2;

import com.hk.core.autoconfigure.exception.Oauth2ErrorController;
import com.hk.stream.binder.rabbit.Oauth2HeaderAuthenticationChannelInterceptor;
import com.hk.stream.binder.rabbit.Oauth2RabbitMessageChannelBinder;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.support.postprocessor.DelegatingDecompressingPostProcessor;
import org.springframework.amqp.support.postprocessor.GZipPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.binder.rabbit.properties.RabbitBinderConfigurationProperties;
import org.springframework.cloud.stream.binder.rabbit.properties.RabbitExtendedBindingProperties;
import org.springframework.cloud.stream.binder.rabbit.provisioning.RabbitExchangeQueueProvisioner;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author huangkai
 * @date 2018-12-27 15:57
 */
@Configuration
@EnableConfigurationProperties(value = {RabbitBinderConfigurationProperties.class, RabbitExtendedBindingProperties.class})
@ConditionalOnClass(value = {OAuth2ClientAuthenticationProcessingFilter.class})
public class Oauth2AutoConfiguration {

    /**
     * 客户端登陆Oauth2 认证出错时的处理器
     */
    @Bean
    public Oauth2ErrorController oauth2ErrorController() {
        return new Oauth2ErrorController();
    }

    /**
     * 此种配置对 stream rabbit 不生效，oauth2 rabbitmq 认证配置
     * <pre>
     *     需要在请求头带上 认证信息 access_token
     * </pre>
     */
//    @Configuration
//    @ConditionalOnBean(value = {ConnectionFactory.class})
//    public class Oauth2RabbitListenerConfiguration {

    @Autowired
    private RabbitProperties rabbitProperties;

    @Autowired
    private ConnectionFactory rabbitConnectionFactory;

    @Autowired
    private RabbitBinderConfigurationProperties rabbitBinderConfigurationProperties;

    @Autowired
    private RabbitExtendedBindingProperties rabbitExtendedBindingProperties;

    @Autowired
    private TokenStore tokenStore;

    @Bean
    MessagePostProcessor deCompressingPostProcessor() {
        return new DelegatingDecompressingPostProcessor();
    }

    @Bean
    MessagePostProcessor gZipPostProcessor() {
        GZipPostProcessor gZipPostProcessor = new GZipPostProcessor();
        gZipPostProcessor.setLevel(this.rabbitBinderConfigurationProperties.getCompressionLevel());
        return gZipPostProcessor;
    }

    @Bean
    RabbitExchangeQueueProvisioner provisioningProvider() {
        return new RabbitExchangeQueueProvisioner(this.rabbitConnectionFactory);
    }

    @Bean
    Oauth2RabbitMessageChannelBinder rabbitMessageChannelBinder(@Nullable ListenerContainerCustomizer<AbstractMessageListenerContainer> listenerContainerCustomizer) throws Exception {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        Oauth2RabbitMessageChannelBinder binder = new Oauth2RabbitMessageChannelBinder(this.rabbitConnectionFactory,
                this.rabbitProperties, defaultTokenServices, provisioningProvider(), listenerContainerCustomizer);
        binder.setAdminAddresses(this.rabbitBinderConfigurationProperties.getAdminAddresses());
        binder.setCompressingPostProcessor(gZipPostProcessor());
        binder.setDecompressingPostProcessor(deCompressingPostProcessor());
        binder.setNodes(this.rabbitBinderConfigurationProperties.getNodes());
        binder.setExtendedBindingProperties(this.rabbitExtendedBindingProperties);
        return binder;
//        }
//

//        @Autowired
//        private ConnectionFactory connectionFactory;
//
//        @Autowired
//        private TokenStore tokenStore;
//
//        @Bean
//        public RabbitListenerConfigurer rabbitListenerConfigurer() {
//            System.out.println("--------");
//            DefaultTokenServices tokenServices = new DefaultTokenServices();
//            tokenServices.setTokenStore(tokenStore);
//            return new Oauth2AuthenticationRabbitListenerConfigurer(connectionFactory, tokenServices);
//        }
//
    }
}

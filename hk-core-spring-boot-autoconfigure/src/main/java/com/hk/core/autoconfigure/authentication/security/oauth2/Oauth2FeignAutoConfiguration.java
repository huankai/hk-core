package com.hk.core.autoconfigure.authentication.security.oauth2;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * @author: kevin
 * @date 2018-08-09 10:29
 */
@Configuration
@ConditionalOnClass(RequestInterceptor.class)
public class Oauth2FeignAutoConfiguration {

    @Bean
    public OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor(OAuth2ClientContext clientContext, OAuth2ProtectedResourceDetails resourceDetails) {
        return new OAuth2FeignRequestInterceptor(clientContext, resourceDetails);
    }


}

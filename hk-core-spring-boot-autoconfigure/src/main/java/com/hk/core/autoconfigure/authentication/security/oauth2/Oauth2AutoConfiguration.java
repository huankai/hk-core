package com.hk.core.autoconfigure.authentication.security.oauth2;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;

import com.hk.core.autoconfigure.exception.Oauth2ErrorController;

/**
 * @author huangkai
 * @date 2018-12-27 15:57
 */
@Configuration
@ConditionalOnClass(value = {OAuth2ClientAuthenticationProcessingFilter.class})
public class Oauth2AutoConfiguration {

    /**
     * 客户端登陆Oauth2 认证出错时的处理器
     */
    @Bean
    public Oauth2ErrorController oauth2ErrorController() {
        return new Oauth2ErrorController();
    }

}

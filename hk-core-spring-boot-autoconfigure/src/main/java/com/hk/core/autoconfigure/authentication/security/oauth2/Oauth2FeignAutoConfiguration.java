package com.hk.core.autoconfigure.authentication.security.oauth2;

import com.hk.core.authentication.api.SecurityContext;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author kevin
 * @date 2018-08-09 10:29
 */
@Configuration
@ConditionalOnClass(RequestInterceptor.class)
@ConditionalOnBean(OAuth2ClientContext.class)
public class Oauth2FeignAutoConfiguration {

    private SecurityContext securityContext;

    public Oauth2FeignAutoConfiguration(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Bean
    public OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor(OAuth2ClientContext clientContext, OAuth2ProtectedResourceDetails resourceDetails) {
        return new OAuth2FeignRequestInterceptor(clientContext, resourceDetails) {

            @Override
            protected String extract(String tokenType) {
                // 如果当前用户没有认证，则调用 其它服务的接口也不能需要认证，否则就会调用失败
                if (!securityContext.isAuthenticated()) {
                    return null;
                }
                var accessToken = getToken();
                return accessToken == null ? null : String.format("%s %s", tokenType, accessToken.getValue());
            }

            @Override
            public OAuth2AccessToken getToken() {
                var accessToken = clientContext.getAccessToken();
                if (accessToken == null || accessToken.isExpired()) {
                    try {
                        accessToken = acquireAccessToken();
                    } catch (UserRedirectRequiredException e) {
                        clientContext.setAccessToken(null);
                        var stateKey = e.getStateKey();
                        if (stateKey != null) {
                            var stateToPreserve = e.getStateToPreserve();
                            if (stateToPreserve == null) {
                                stateToPreserve = "NONE";
                            }
                            clientContext.setPreservedState(stateKey, stateToPreserve);
                        }
                        // 查看父类方法，当使用Feign 调用不需要认证的api时，此时用户未授权(没有带 access_token) 参数，在这里获取认证信息时会抛出异常
                        // 解决方法: 如果不需要(或不能)获取用户认证信息，则不带请求认证头信息(Authorization)到调用的服务提供方
                        return null;
                    } catch (RuntimeException e) {
                        return null;
                    }
                }
                return accessToken;
            }
        };
    }

}

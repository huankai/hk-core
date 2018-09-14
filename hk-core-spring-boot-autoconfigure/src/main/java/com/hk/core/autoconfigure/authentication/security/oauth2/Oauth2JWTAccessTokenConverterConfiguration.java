package com.hk.core.autoconfigure.authentication.security.oauth2;

import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.oauth2.converter.Oauth2UserAuthenticationConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Oauth2 返回信息封装到 {@link UserPrincipal } 对象中自动配置
 *
 * @author: kevin
 * @date: 2018-08-01 16:29
 */
@Configuration
@ConditionalOnClass(Oauth2UserAuthenticationConverter.class)
public class Oauth2JWTAccessTokenConverterConfiguration implements JwtAccessTokenConverterConfigurer {

    @Override
    public void configure(JwtAccessTokenConverter converter) {
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new Oauth2UserAuthenticationConverter());
        converter.setAccessTokenConverter(accessTokenConverter);
    }
}

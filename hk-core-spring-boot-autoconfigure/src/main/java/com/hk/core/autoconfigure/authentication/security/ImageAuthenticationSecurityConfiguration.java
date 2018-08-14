package com.hk.core.autoconfigure.authentication.security;

import com.hk.core.authentication.api.validatecode.ImageCodeGenerator;
import com.hk.core.authentication.api.validatecode.ImageCodeProcessor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 图片验证码配置
 *
 * @author: kevin
 * @date 2018-07-27 16:00
 */
public class ImageAuthenticationSecurityConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthenticationProperties.ImageCodeProperties imageCodeProperties;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final String loginProcessingUrl;

    public ImageAuthenticationSecurityConfiguration(AuthenticationProperties.ImageCodeProperties imageCodeProperties,
                                                    AuthenticationFailureHandler failureHandler, String loginProcessingUrl) {
        this.imageCodeProperties = imageCodeProperties;
        this.authenticationFailureHandler = failureHandler;
        this.loginProcessingUrl = loginProcessingUrl;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (imageCodeProperties.isEnabled()) {
            ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator(imageCodeProperties.getWidth(), imageCodeProperties.getHeight(),
                    imageCodeProperties.getCodeLength(), imageCodeProperties.getCodeExpireIn());
            ImageCodeProcessor imageCodeProcessor = new ImageCodeProcessor(imageCodeGenerator, imageCodeProperties.getCodeParameter());
//            http.apply(new ValidateCodeSecurityConfiguration(new ValidateCodeFilter(authenticationFailureHandler, imageCodeProcessor, loginProcessingUrl)));
        }
    }
}

package com.hk.core.authentication.security.config;

import com.hk.core.authentication.security.SpringSecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityAuthenticationAutoConfiguration {

    @Bean
    public SpringSecurityContext securityContext() {
        return new SpringSecurityContext();
    }

}

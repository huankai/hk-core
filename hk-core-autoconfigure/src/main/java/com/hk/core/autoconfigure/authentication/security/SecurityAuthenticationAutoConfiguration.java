package com.hk.core.autoconfigure.authentication.security;

import com.hk.core.authentication.security.SpringSecurityContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConditionalOnClass(value = {SpringSecurityContext.class, PasswordEncoder.class})
public class SecurityAuthenticationAutoConfiguration {

    /**
     * SecurityContext
     *
     * @return
     */
    @Bean
    public SpringSecurityContext securityContext() {
        return new SpringSecurityContext();
    }

    /**
     * 密码编码器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

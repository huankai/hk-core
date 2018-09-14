package com.hk.core.autoconfigure.exception;

import com.hk.core.authentication.security.SpringSecurityContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: kevin
 * @date: 2018-09-10 14:38
 */
@Configuration
public class ExceptionHandlerConfiguration {

    @Bean
    public SimpleExceptionHandler simpleExceptionHandler() {
        return new SimpleExceptionHandler();
    }

    @Configuration
    @ConditionalOnClass(SpringSecurityContext.class)
    public class SpringSecurityExceptionHandlerConfiguration {

        @Bean
        public SpringSecurityExceptionHandler springSecurityExceptionHandler() {
            return new SpringSecurityExceptionHandler();
        }

    }
}

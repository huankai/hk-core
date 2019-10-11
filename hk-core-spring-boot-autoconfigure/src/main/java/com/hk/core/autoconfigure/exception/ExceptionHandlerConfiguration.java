package com.hk.core.autoconfigure.exception;

import com.hk.core.authentication.security.SpringSecurityContext;
import com.hk.core.service.exception.ServiceException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityNotFoundException;

/**
 * 异常处理器配置
 *
 * @author kevin
 * @date 2018-09-10 14:38
 */
@Configuration
public class ExceptionHandlerConfiguration {

    /**
     * 公用异常处理
     *
     */
    @Bean
    public SimpleExceptionHandler simpleExceptionHandler() {
        return new SimpleExceptionHandler();
    }


    /**
     * Service Exception
     */
    @Configuration
    @ConditionalOnClass(value = {ServiceException.class})
    static class ServiceExceptionHandlerConfiguration {

        @Bean
        public ServiceExceptionHandler jdbcExceptionHandler() {
            return new ServiceExceptionHandler();
        }
    }

    /**
     * Jdbc Exception
     */
    @Configuration
    @ConditionalOnClass(value = {com.hk.core.data.jdbc.exception.EntityNotFoundException.class})
    static class JdbcExceptionHandlerConfiguration {

        @Bean
        public JdbcExceptionHandler jdbcExceptionHandler() {
            return new JdbcExceptionHandler();
        }
    }

    /**
     * Jpa　Exception
     */
    @Configuration
    @ConditionalOnClass(value = {EntityNotFoundException.class})
    static class JpaExceptionHandlerConfiguration {

        @Bean
        public JpaExceptionHandler jpaExceptionHandler() {
            return new JpaExceptionHandler();
        }

    }

    /**
     * Spring Security Exception
     */
    @Configuration
    @ConditionalOnClass(value = {SpringSecurityContext.class})
    static class SpringSecurityExceptionHandlerConfiguration {

        @Bean
        public SpringSecurityExceptionHandler springSecurityExceptionHandler() {
            return new SpringSecurityExceptionHandler();
        }
    }
}

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
     * @return
     */
    @Bean
    public SimpleExceptionHandler simpleExceptionHandler() {
        return new SimpleExceptionHandler();
    }


    /**
     * Service Exception
     */
    @Configuration
    @ConditionalOnClass(ServiceException.class)
    public class ServiceExceptionHandlerConfiguration {

        @Bean
        public ServiceExceptionHandler jdbcExceptionHandler() {
            return new ServiceExceptionHandler();
        }
    }

    /**
     * Jdbc Exception
     */
    @Configuration
    @ConditionalOnClass(com.hk.core.data.jdbc.exception.EntityNotFoundException.class)
    public class JdbcExceptionHandlerConfiguration {

        @Bean
        public JdbcExceptionHandler jdbcExceptionHandler() {
            return new JdbcExceptionHandler();
        }
    }

    /**
     * Jpa　Exception
     */
    @Configuration
    @ConditionalOnClass(EntityNotFoundException.class)
    public class JpaExceptionHandlerConfiguration {

        @Bean
        public JpaExceptionHandler jpaExceptionHandler() {
            return new JpaExceptionHandler();
        }

    }

    /**
     * Spring Security Exception
     */
    @Configuration
    @ConditionalOnClass(SpringSecurityContext.class)
    public class SpringSecurityExceptionHandlerConfiguration {

        @Bean
        public SpringSecurityExceptionHandler springSecurityExceptionHandler() {
            return new SpringSecurityExceptionHandler();
        }
    }
}

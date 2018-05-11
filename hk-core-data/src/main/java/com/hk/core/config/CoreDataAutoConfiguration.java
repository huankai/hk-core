package com.hk.core.config;

import com.hk.core.audit.UserAuditorAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.query.jdbc.JdbcSession;
import com.hk.core.query.jdbc.dialect.Dialect;
import com.hk.core.query.jdbc.dialect.MySQLDialect;

/**
 * Core Dao 自动配置
 *
 * @author huangkai
 */
@Configuration
@EnableJpaAuditing
public class CoreDataAutoConfiguration {

    @Bean
    public JdbcSession jdbcSession(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                   Dialect dialect) {
        return new JdbcSession(jdbcTemplate, namedParameterJdbcTemplate, dialect);
    }

    /**
     * 使用的数据库，默认为MYSQL
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(value = Dialect.class)
    public Dialect Dialect() {
        return new MySQLDialect();
    }

	/* ************ audit ************ */

    /**
     * jpa audit功能
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(value = {SecurityContext.class})
    @ConditionalOnMissingBean(value = AuditorAware.class)
    public AuditorAware<?> userIdAuditor(SecurityContext securityContext) {
        return new UserAuditorAware(securityContext);
    }
}

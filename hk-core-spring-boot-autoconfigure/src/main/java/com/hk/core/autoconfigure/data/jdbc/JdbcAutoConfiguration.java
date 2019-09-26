package com.hk.core.autoconfigure.data.jdbc;

import com.hk.core.jdbc.JdbcSession;
import com.hk.core.jdbc.dialect.Dialect;
import com.hk.core.jdbc.dialect.MysqlDialect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author kevin
 * @date 2019-9-11 20:55
 */
@Configuration
@ConditionalOnClass(JdbcSession.class)
public class JdbcAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Dialect.class)
    public Dialect mysqlDialect() {
        return new MysqlDialect();
    }

    @Bean
    public JdbcSession jdbcSession(NamedParameterJdbcTemplate jdbcTemplate, Dialect dialect) {
        return new JdbcSession(jdbcTemplate, dialect);
    }
}

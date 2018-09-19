package com.hk.core.autoconfigure.data.jdbc;

import com.hk.core.data.jdbc.JdbcSession;
import com.hk.core.data.jdbc.dialect.Dialect;
import com.hk.core.data.jdbc.dialect.MysqlDialect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author: kevin
 * @date: 2018-09-19 10:18
 */
@Configuration
@ConditionalOnClass(JdbcSession.class)
public class JdbcAutoConfiguration {

    @Bean
    public JdbcSession jdbcSession(NamedParameterJdbcTemplate namedParameterJdbcTemplate, Dialect dialect) {
        return new JdbcSession(namedParameterJdbcTemplate, dialect);
    }

    @Bean
    @ConditionalOnMissingBean(Dialect.class)
    public Dialect mysqlDialect() {
        return new MysqlDialect();
    }
}

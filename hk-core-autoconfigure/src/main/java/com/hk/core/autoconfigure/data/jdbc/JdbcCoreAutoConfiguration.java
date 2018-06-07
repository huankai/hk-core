package com.hk.core.autoconfigure.data.jdbc;

import com.hk.core.query.jdbc.JdbcSession;
import com.hk.core.query.jdbc.dialect.Dialect;
import com.hk.core.query.jdbc.dialect.MySQLDialect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Core Dao 自动配置
 *
 * @author huangkai
 */
@Configuration
@ConditionalOnClass(JdbcSession.class)
public class JdbcCoreAutoConfiguration {

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


}

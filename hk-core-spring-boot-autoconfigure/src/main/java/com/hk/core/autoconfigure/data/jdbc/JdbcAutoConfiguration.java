package com.hk.core.autoconfigure.data.jdbc;

import com.hk.commons.util.IDGenerator;
import com.hk.core.data.jdbc.*;
import com.hk.core.data.jdbc.dialect.Dialect;
import com.hk.core.data.jdbc.dialect.MysqlDialect;
import com.hk.core.data.jdbc.domain.AbstractUUIDPersistable;
import com.hk.core.data.jdbc.metadata.PersistentEntityMetadata;
import com.hk.core.data.jdbc.metadata.SpringJdbcPersistentEntityMetadata;
import com.hk.core.data.jdbc.repository.BaseJdbcRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jdbc.repository.config.JdbcConfiguration;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


/**
 * @author: kevin
 * @date: 2018-09-19 10:18
 */
@Configuration
@EnableJdbcAuditing
@EnableJdbcRepositories(basePackages = {"com.hk.**.repository.jdbc"}, repositoryFactoryBeanClass = BaseJdbcRepositoryFactoryBean.class)
@ConditionalOnClass(JdbcSession.class)
@Import(JdbcConfiguration.class)
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

    /**
     * @see BaseJdbcRepository#getPersistentEntityMetadata()
     */
    @Bean
    @ConditionalOnMissingBean(PersistentEntityMetadata.class)
    public PersistentEntityMetadata persistentEntityMetadata() {
        return new SpringJdbcPersistentEntityMetadata();
    }

    /**
     * 保存之前生成主键Id监听器
     *
     * @return ApplicationListener
     * @see https://docs.spring.io/spring-data/jdbc/docs/1.0.0.RELEASE/reference/html/#core.domain-events
     */
    @Bean
    public ApplicationListener<BeforeSaveEvent> generatePrimaryKeyListener() {
        return event -> {
            Object entity = event.getEntity();
            if (entity instanceof AbstractUUIDPersistable) {
                AbstractUUIDPersistable persisTable = (AbstractUUIDPersistable) entity;
                if (persisTable.isNew()) {
                    persisTable.generateId(IDGenerator.STRING_UUID.generate());
                }
            }
        };
    }

}

package com.hk.core.autoconfigure.data.jdbc;

import com.hk.commons.util.Lazy;
import com.hk.commons.util.SnowflakeIdGenerator;
import com.hk.commons.util.SpringContextHolder;
import com.hk.core.data.commons.properties.SnowflakeProperties;
import com.hk.core.data.jdbc.repository.BaseJdbcRepositoryFactoryBean;
import com.hk.core.data.jpa.domain.AbstractSnowflakeIdPersistable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jdbc.repository.config.JdbcConfiguration;
import org.springframework.data.relational.core.conversion.RelationalConverter;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;

import java.util.Optional;


/**
 * @author kevin
 * @date 2018-09-19 10:18
 */
@Configuration
@ConditionalOnClass(BaseJdbcRepositoryFactoryBean.class)
@EnableJdbcAuditing
@EnableJdbcRepositories(basePackages = {"**.repository.jdbc"}, repositoryFactoryBeanClass = BaseJdbcRepositoryFactoryBean.class)
public class DataJdbcAutoConfiguration extends JdbcConfiguration {

//    /**
//     * 如果是使用 spring data common中的注解映射表名和字段名，配置此bean，默认会配置
//     */
//    @Bean
//    @ConditionalOnMissingBean(PersistentEntityMetadata.class)
//    public PersistentEntityMetadata persistentEntityMetadata() {
//        return new SpringJdbcPersistentEntityMetadata();
//    }

    /* ************如果是使用 jpa 注解映射表名和字段名，配置下面两个 bean(JpaPersistentEntityMetadata) 和 (JpaAnnotationNamingStrategy),*************** */

//    @Bean
//    public PersistentEntityMetadata jpaPersistentEntityMetadata() {
//        return new JpaPersistentEntityMetadata();
//    }
//
//    @Bean
//    public JpaAnnotationNamingStrategy namingStrategy() {
//        return new JpaAnnotationNamingStrategy();
//    }

    @Override
    protected RelationalMappingContext jdbcMappingContext(Optional<NamingStrategy> namingStrategy) {
        return super.jdbcMappingContext(namingStrategy);
    }

    @Override
    protected RelationalConverter relationalConverter(RelationalMappingContext mappingContext) {
        return super.relationalConverter(mappingContext);
    }

    @Override
    protected JdbcCustomConversions jdbcCustomConversions() {
        return super.jdbcCustomConversions();
    }

    /**
     * 保存之前生成主键Id监听器
     * @see https://docs.spring.io/spring-data/jdbc/docs/1.0.0.RELEASE/reference/html/#core.domain-events
     */
    private static class GenerateIdApplicationListener implements ApplicationListener<BeforeSaveEvent> {

        private final Lazy<SnowflakeIdGenerator> SNOWFLAKE_ID_GENERATOR = Lazy.of(() -> SpringContextHolder.getBeanIfExist(SnowflakeProperties.class)
                .map(item -> new SnowflakeIdGenerator(item.getWorkerId(), item.getDataCenterId()))
                .orElse(new SnowflakeIdGenerator()));

        @Override
        public void onApplicationEvent(BeforeSaveEvent event) {
            Object entity = event.getEntity();
            if (entity instanceof AbstractSnowflakeIdPersistable) {
                AbstractSnowflakeIdPersistable persis = (AbstractSnowflakeIdPersistable) entity;
                if (persis.isNew()) {
                    persis.setId(SNOWFLAKE_ID_GENERATOR.get().generate());
                }
            }
        }
    }


    @Bean
    public ApplicationListener<BeforeSaveEvent> generatePrimaryKeyListener() {
        return new GenerateIdApplicationListener();
    }


}

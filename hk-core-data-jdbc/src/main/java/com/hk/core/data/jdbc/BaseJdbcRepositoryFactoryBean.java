package com.hk.core.data.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.jdbc.core.DataAccessStrategy;
import org.springframework.data.jdbc.core.DefaultDataAccessStrategy;
import org.springframework.data.jdbc.core.SqlGeneratorSource;
import org.springframework.data.jdbc.repository.RowMapperMap;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactoryBean;
import org.springframework.data.relational.core.conversion.RelationalConverter;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * @author: sjq-278
 * @date: 2018-10-11 13:29
 * @see JdbcRepositoryFactoryBean
 */
public class BaseJdbcRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;
    private RelationalMappingContext mappingContext;
    private RelationalConverter converter;
    private DataAccessStrategy dataAccessStrategy;
    private RowMapperMap rowMapperMap = RowMapperMap.EMPTY;
    private NamedParameterJdbcOperations operations;

    /**
     * Creates a new {@link JdbcRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    BaseJdbcRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {

        super.setApplicationEventPublisher(publisher);

        this.publisher = publisher;
    }

    /**
     * 创建自定义的 BaseJdbcRepositoryFactory
     * Creates the actual {@link RepositoryFactorySupport} instance.
     */
    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {
        BaseJdbcRepositoryFactory jdbcRepositoryFactory = new BaseJdbcRepositoryFactory(dataAccessStrategy, mappingContext,
                converter, publisher, operations);
        jdbcRepositoryFactory.setRowMapperMap(rowMapperMap);
        return jdbcRepositoryFactory;
    }

    @Autowired
    protected void setMappingContext(RelationalMappingContext mappingContext) {

        super.setMappingContext(mappingContext);
        this.mappingContext = mappingContext;
    }

    /**
     * @param dataAccessStrategy can be {@literal null}.
     */
    @Autowired(required = false)
    public void setDataAccessStrategy(DataAccessStrategy dataAccessStrategy) {
        this.dataAccessStrategy = dataAccessStrategy;
    }

    /**
     * @param rowMapperMap can be {@literal null}. {@link #afterPropertiesSet()} defaults to {@link RowMapperMap#EMPTY} if
     *                     {@literal null}.
     */
    @Autowired(required = false)
    public void setRowMapperMap(RowMapperMap rowMapperMap) {
        this.rowMapperMap = rowMapperMap;
    }

    @Autowired
    public void setJdbcOperations(NamedParameterJdbcOperations operations) {
        this.operations = operations;
    }

    @Autowired
    public void setConverter(RelationalConverter converter) {
        this.converter = converter;
    }

    public void afterPropertiesSet() {

        Assert.state(this.mappingContext != null, "MappingContext is required and must not be null!");
        Assert.state(this.converter != null, "RelationalConverter is required and must not be null!");
        if (dataAccessStrategy == null) {

            SqlGeneratorSource sqlGeneratorSource = new SqlGeneratorSource(mappingContext);
            this.dataAccessStrategy = new DefaultDataAccessStrategy(sqlGeneratorSource, mappingContext, converter,
                    operations);
        }

        if (rowMapperMap == null) {
            this.rowMapperMap = RowMapperMap.EMPTY;
        }

        super.afterPropertiesSet();
    }


}

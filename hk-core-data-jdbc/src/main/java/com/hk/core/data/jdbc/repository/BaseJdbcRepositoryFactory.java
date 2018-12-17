package com.hk.core.data.jdbc.repository;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.DataAccessStrategy;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
import org.springframework.data.relational.core.conversion.RelationalConverter;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

/**
 * 构建自定义的 JdbcRepository
 *
 * @author kevin
 * @date 2018-10-11 14:17
 */
public class BaseJdbcRepositoryFactory extends JdbcRepositoryFactory {

    private final RelationalMappingContext context;

    private final RelationalConverter converter;

    private final ApplicationEventPublisher publisher;

    private final DataAccessStrategy accessStrategy;

    /**
     * Creates a new {@link JdbcRepositoryFactory} for the given {@link DataAccessStrategy},
     * {@link RelationalMappingContext} and {@link ApplicationEventPublisher}.
     *
     * @param dataAccessStrategy must not be {@literal null}.
     * @param context            must not be {@literal null}.
     * @param converter          must not be {@literal null}.
     * @param publisher          must not be {@literal null}.
     * @param operations         must not be {@literal null}.
     */
    BaseJdbcRepositoryFactory(DataAccessStrategy dataAccessStrategy, RelationalMappingContext context, RelationalConverter converter, ApplicationEventPublisher publisher, NamedParameterJdbcOperations operations) {
        super(dataAccessStrategy, context, converter, publisher, operations);
        this.context = context;
        this.converter = converter;
        this.publisher = publisher;
        this.accessStrategy = dataAccessStrategy;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation repositoryInformation) {
        JdbcAggregateTemplate template = new JdbcAggregateTemplate(publisher, context, converter, accessStrategy);
        return new BaseJdbcRepository<>(template, context.getPersistentEntity(repositoryInformation.getDomainType()));
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata repositoryMetadata) {
        return BaseJdbcRepository.class;
    }
}

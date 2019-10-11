package com.hk.core.elasticsearch.repository;

import org.springframework.data.domain.Persistable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;


/**
 * ElasticsearchRepository Factory Bean
 *
 * @param <T>
 * @param <S>
 * @see ElasticsearchRepositoryFactoryBean
 */
public class BaseElasticsearchRepositoryFactoryBean<T extends BaseElasticsearchRepository<S>, S extends Persistable<String>>
        extends ElasticsearchRepositoryFactoryBean<T, S, String> {

    private ElasticsearchOperations operations;

    /**
     * Creates a new {@link ElasticsearchRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public BaseElasticsearchRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    /**
     * Configures the {@link ElasticsearchOperations} to be used to create Elasticsearch repositories.
     *
     * @param operations the operations to set
     */
    public void setElasticsearchOperations(ElasticsearchOperations operations) {
        super.setElasticsearchOperations(operations);
        this.operations = operations;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(operations, "ElasticsearchOperations must be configured!");
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory() {
        return new BaseElasticsearchRepositoryFactory(operations);
    }

}

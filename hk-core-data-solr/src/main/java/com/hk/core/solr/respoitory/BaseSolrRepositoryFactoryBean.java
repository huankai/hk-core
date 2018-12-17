package com.hk.core.solr.respoitory;

import org.apache.solr.client.solrj.SolrClient;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.convert.SolrConverter;
import org.springframework.data.solr.core.mapping.SimpleSolrMappingContext;
import org.springframework.data.solr.repository.support.SolrRepositoryFactoryBean;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * @author sjq-278
 * @date 2018-12-03 13:09
 * @see SolrRepositoryFactoryBean
 */
public class BaseSolrRepositoryFactoryBean<T extends BaseSolrRepository<S, ID>, S extends Serializable, ID extends Serializable> extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> {

    @Nullable
    private SolrClient solrClient;

    @Nullable
    private SolrOperations operations;

    private boolean schemaCreationSupport;

    @Nullable
    private SimpleSolrMappingContext solrMappingContext;

    @Nullable
    private SolrConverter solrConverter;


    /**
     * Creates a new {@link SolrRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public BaseSolrRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    /**
     * Configures the {@link SolrOperations} to be used to create Solr repositories.
     *
     * @param operations the operations to set
     */
    public void setSolrOperations(SolrOperations operations) {
        this.operations = operations;
    }

    public void setSolrClient(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    public void setSchemaCreationSupport(boolean schemaCreationSupport) {
        this.schemaCreationSupport = schemaCreationSupport;
    }

    /**
     * @param solrConverter
     * @since 2.1
     */
    public void setSolrConverter(SolrConverter solrConverter) {
        this.solrConverter = solrConverter;
    }

    /**
     * @param solrMappingContext
     * @since 1.4
     */
    public void setSolrMappingContext(SimpleSolrMappingContext solrMappingContext) {
        this.solrMappingContext = solrMappingContext;
        super.setMappingContext(solrMappingContext);
    }

    /**
     * @return
     * @since 1.4
     */
    @Nullable
    public SimpleSolrMappingContext getSolrMappingContext() {
        return solrMappingContext;
    }

    /**
     * @return SolrOperations to be used for eg. custom implementation
     */
    @Nullable
    protected SolrOperations getSolrOperations() {
        return this.operations;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.isTrue((operations != null || solrClient != null), "SolrOperations or SolrClient must be configured!");
    }

    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {
        BaseSolrRepositoryFactory factory = operations != null ? new BaseSolrRepositoryFactory(this.operations) :
                new BaseSolrRepositoryFactory(this.solrClient, solrConverter);
        factory.setSchemaCreationSupport(schemaCreationSupport);
        return factory;
    }
}

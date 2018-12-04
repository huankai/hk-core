package com.hk.core.solr.respoitory;

import org.apache.solr.client.solrj.SolrClient;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.convert.SolrConverter;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;

/**
 * @author: sjq-278
 * @date: 2018-12-03 13:16
 */
public class BaseSolrRepositoryFactory extends SolrRepositoryFactory {

    public BaseSolrRepositoryFactory(SolrOperations solrOperations) {
        super(solrOperations);
    }

    public BaseSolrRepositoryFactory(SolrClient solrClient) {
        super(solrClient);
    }

    public BaseSolrRepositoryFactory(SolrClient solrClient, SolrConverter converter) {
        super(solrClient, converter);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        super.getRepositoryBaseClass(metadata);
        return BaseSimpleSolrRepository.class;
    }
}

package com.hk.core.elasticsearch.repository;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory;
import org.springframework.data.elasticsearch.repository.support.NumberKeyedRepository;
import org.springframework.data.elasticsearch.repository.support.UUIDElasticsearchRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;

import java.util.UUID;

import static org.springframework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;


public class BaseElasticsearchRepositoryFactory extends ElasticsearchRepositoryFactory {

    public BaseElasticsearchRepositoryFactory(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if (isQueryDslRepository(metadata.getRepositoryInterface())) {
            throw new IllegalArgumentException("QueryDsl Support has not been implemented yet.");
        }
        if (Integer.class.isAssignableFrom(metadata.getIdType()) || Long.class.isAssignableFrom(metadata.getIdType())
                || Double.class.isAssignableFrom(metadata.getIdType())) {
            return NumberKeyedRepository.class;
        } else if (metadata.getIdType() == String.class) {
            // 使用自定义扩展的 Repository
            return SimpleBaseElasticsearchRepository.class;
        } else if (metadata.getIdType() == UUID.class) {
            return UUIDElasticsearchRepository.class;
        } else {
            throw new IllegalArgumentException("Unsupported ID type " + metadata.getIdType());
        }
    }

    private static boolean isQueryDslRepository(Class<?> repositoryInterface) {
        return QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
    }
}

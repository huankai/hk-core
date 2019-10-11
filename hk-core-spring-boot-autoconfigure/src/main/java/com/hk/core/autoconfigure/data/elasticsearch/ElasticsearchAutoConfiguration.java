package com.hk.core.autoconfigure.data.elasticsearch;

import org.elasticsearch.client.ElasticsearchClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.hk.core.elasticsearch.repository.BaseElasticsearchRepositoryFactoryBean;

/**
 * Elasticsearch 配置类
 *
 * @author huangkai
 * @see org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration
 */
@Configuration
@ConditionalOnClass(ElasticsearchClient.class)
@EnableElasticsearchRepositories(basePackages = {"**.repository.elasticsearch"}, repositoryFactoryBeanClass = BaseElasticsearchRepositoryFactoryBean.class)
public class ElasticsearchAutoConfiguration {


}

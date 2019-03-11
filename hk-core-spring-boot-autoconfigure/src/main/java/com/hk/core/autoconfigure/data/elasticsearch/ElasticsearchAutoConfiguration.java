package com.hk.core.autoconfigure.data.elasticsearch;

import com.hk.core.elasticsearch.repository.BaseElasticsearchRepositoryFactoryBean;
import org.elasticsearch.client.ElasticsearchClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

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

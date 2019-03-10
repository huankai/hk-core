package com.hk.core.autoconfigure.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.ElasticsearchClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch 配置类
 * 
 * @author huangkai
 *
 */
@Configuration
@ConditionalOnClass(ElasticsearchClient.class)
@EnableElasticsearchRepositories(basePackages = { "**.repository.elasticsearch" })
public class ElasticsearchAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(ElasticsearchTemplate.class)
	public ElasticsearchTemplate elasticsearchTemplate(Client client) {
		return new ElasticsearchTemplate(client);
	}
}

package com.hk.core.autoconfigure.data.elasticsearch;

import com.hk.core.elasticsearch.repository.BaseElasticsearchRepositoryFactoryBean;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch 配置类
 *
 * @author huangkai
 * @see org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration
 */
@Configuration
@ConditionalOnClass(RestHighLevelClient.class)
@EnableConfigurationProperties(value = {RestClientProperties.class})
@EnableElasticsearchRepositories(basePackages = {"**.repository.elasticsearch"}, repositoryFactoryBeanClass = BaseElasticsearchRepositoryFactoryBean.class)
public class ElasticsearchAutoConfiguration extends AbstractElasticsearchConfiguration {

    private final RestClientProperties restClientProperties;

    public ElasticsearchAutoConfiguration(RestClientProperties restClientProperties) {
        this.restClientProperties = restClientProperties;
    }

    @Override
    public RestHighLevelClient elasticsearchClient() {
        var configuration = ClientConfiguration.builder()
                .connectedTo(restClientProperties.getUris().toArray(new String[0]))
                .usingSsl()
                .withBasicAuth(restClientProperties.getUsername(), restClientProperties.getPassword())
                .withConnectTimeout(restClientProperties.getConnectionTimeout())
                .withSocketTimeout(restClientProperties.getReadTimeout())
                .build();
        return RestClients.create(configuration).rest();
    }
}

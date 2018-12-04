package com.hk.core.autoconfigure.data.solr;

import com.hk.core.solr.SolrDeltaImportUtils;
import com.hk.core.solr.aop.SolrDeltaImportMethodInterceptor;
import com.hk.core.solr.respoitory.BaseSolrRepositoryFactoryBean;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

/**
 * Solr 增量导入自动配置
 *
 * @author: kevin
 * @date: 2018-07-04 12:24
 */
@Configuration
@ConditionalOnClass(SolrClient.class)
@EnableSolrRepositories(basePackages = {"com.hk.**.repository.solr"}, repositoryFactoryBeanClass = BaseSolrRepositoryFactoryBean.class)
public class SolrAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SolrTemplate.class)
    public SolrTemplate solrTemplate(SolrClient solrClient) {
        return new SolrTemplate(solrClient);
    }

    /**
     * Aspect 自动配置
     */
    @Configuration
    @ConditionalOnProperty(prefix = "spring.data.solr", name = "enable-delta-import", havingValue = "true")
    @ConditionalOnClass(SolrDeltaImportUtils.class)
    @EnableConfigurationProperties(SolrProperties.class)
    static class SolrDeltaImportAspectAutoConfiguration {

        private SolrProperties solrProperties;

        public SolrDeltaImportAspectAutoConfiguration(SolrProperties solrProperties) {
            this.solrProperties = solrProperties;
        }

        @Bean(value = "solrAdvisor")
        public Advisor solrAdvisor() {
            SolrDeltaImportMethodInterceptor interceptor = new SolrDeltaImportMethodInterceptor();
            interceptor.setSolrCore(solrProperties.getSolrCore());
            interceptor.setSolrUrl(solrProperties.getHost());
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("@annotation(com.hk.core.solr.SolrDeltaImport)");
            return new DefaultPointcutAdvisor(pointcut, interceptor);
        }
    }

}

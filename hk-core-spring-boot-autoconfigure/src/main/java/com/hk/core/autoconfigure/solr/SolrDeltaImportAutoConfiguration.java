package com.hk.core.autoconfigure.solr;

import com.hk.core.solr.SolrDeltaImportUtils;
import com.hk.core.solr.aop.SolrDeltaImportMethodInterceptor;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;

/**
 * Solr 增量导入自动配置
 *
 * @author: kevin
 * @date: 2018-07-04 12:24
 */
@Configuration
@ConditionalOnClass(SolrDeltaImportUtils.class)
public class SolrDeltaImportAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SolrTemplate.class)
    public SolrTemplate solrTemplate(SolrClient solrClient) {
        return new SolrTemplate(solrClient);
    }

    /**
     * Aspect 自动配置
     */
    @Configuration
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

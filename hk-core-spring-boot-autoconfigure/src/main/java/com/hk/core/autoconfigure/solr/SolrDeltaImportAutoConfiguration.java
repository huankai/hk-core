package com.hk.core.autoconfigure.solr;

import com.hk.core.solr.SolrDeltaImportUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
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
    @Aspect
    @Configuration
    @ConditionalOnClass(SolrDeltaImportUtils.class)
    @EnableConfigurationProperties(SolrProperties.class)
    static class SolrDeltaImportAspectAutoConfiguration {

        private SolrProperties solrProperties;

        public SolrDeltaImportAspectAutoConfiguration(SolrProperties solrProperties) {
            this.solrProperties = solrProperties;
        }

        /**
         * 后置增强
         *
         * @param joinPoint
         */
        @AfterReturning(pointcut = "@annotation(com.hk.core.solr.SolrDeltaImport)")
        public void afterReturning(JoinPoint joinPoint) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            SolrDeltaImportUtils.simpleGetDeltaImport(signature.getMethod(), solrProperties.getHost(), solrProperties.getSolrCore());
        }
    }

}

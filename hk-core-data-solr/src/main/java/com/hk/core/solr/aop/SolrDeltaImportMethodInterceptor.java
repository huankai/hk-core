package com.hk.core.solr.aop;

import com.hk.commons.util.ArrayUtils;
import com.hk.core.solr.SolrDeltaImport;
import com.hk.core.solr.SolrDeltaImportUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Solr 增量导入 Aop 拦截器
 *
 * @author kevin
 * @date 2018-09-19 09:36
 */
public class SolrDeltaImportMethodInterceptor implements AfterReturningAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrDeltaImportMethodInterceptor.class);

    /**
     * SolrUrl
     */
    private String solrUrl;

    /**
     * Solr Core
     */
    private String solrCore;

    @Override
    public synchronized void afterReturning(Object returnValue, Method method, Object[] args, Object target) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("增量同步索引...");
        }
        SolrDeltaImport solrDeltaImport = AnnotationUtils.getAnnotation(method, SolrDeltaImport.class);
        String[] entities = solrDeltaImport.entities();
        if (ArrayUtils.isNotEmpty(entities)) {
            Arrays.stream(entities).forEach(entity -> {
                String result = SolrDeltaImportUtils.simpleGetDeltaImport(solrUrl, solrCore, entity);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("SolrDeltaImport : Send Solr Core :{},entityName :{}, return :{}", solrCore, entity, result);
                }
            });
        }
    }

    public void setSolrCore(String solrCore) {
        this.solrCore = solrCore;
    }

    public void setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
    }
}

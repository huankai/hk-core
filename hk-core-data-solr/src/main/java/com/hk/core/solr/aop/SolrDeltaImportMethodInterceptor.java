package com.hk.core.solr.aop;

import java.lang.reflect.Method;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.core.annotation.AnnotationUtils;

import com.hk.commons.util.ArrayUtils;
import com.hk.core.solr.SolrDeltaImport;
import com.hk.core.solr.SolrDeltaImportUtils;

/**
 * Solr 增量导入 Aop 拦截器
 *
 * @author kevin
 * @date 2018-09-19 09:36
 */
@Slf4j
public class SolrDeltaImportMethodInterceptor implements AfterReturningAdvice {

    /**
     * SolrUrl
     */
    private String solrUrl;

    /**
     * Solr Core
     */
    private String solrCore;

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) {
        if (log.isDebugEnabled()) {
            log.debug("增量同步索引...");
        }
        SolrDeltaImport solrDeltaImport = AnnotationUtils.getAnnotation(method, SolrDeltaImport.class);
        String[] entities = solrDeltaImport.entities();
        if (ArrayUtils.isNotEmpty(entities)) {
            Arrays.stream(entities).forEach(entity -> {
                String result = SolrDeltaImportUtils.simpleGetDeltaImport(solrUrl, solrCore, entity);
                if (log.isInfoEnabled()) {
                    log.info("SolrDeltaImport : Send Solr Core :{},entityName :{}, return :{}", solrCore, entity, result);
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

package com.hk.core.solr.aop;

import com.hk.commons.http.async.AsyncHttpClientUtils;
import com.hk.commons.http.utils.HttpUtils;
import com.hk.commons.util.ArrayUtils;
import com.hk.core.solr.SolrDeltaImport;
import com.hk.core.solr.SolrDeltaImportUtils;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;

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
    @Setter
    private String solrUrl;

    /**
     * Solr Core
     */
    @Setter
    private String solrCore;

    @Override
    @SneakyThrows
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) {
        if (log.isDebugEnabled()) {
            log.debug("增量同步索引...");
        }
        SolrDeltaImport solrDeltaImport = AnnotationUtils.getAnnotation(method, SolrDeltaImport.class);
        if (null != solrDeltaImport) {
            String[] entities = solrDeltaImport.entities();
            if (ArrayUtils.isNotEmpty(entities)) {
                String dataImportUrl = SolrDeltaImportUtils.getDataImportUrl(solrUrl, solrCore);
                try (CloseableHttpAsyncClient httpAsyncClient = AsyncHttpClientUtils.createDefaultHttpAsyncClient()) {
                    httpAsyncClient.start();
                    for (String entity : entities) {
                        Map<String, Object> importParams = SolrDeltaImportUtils.getDeltaImportParams(entity);
                        HttpResponse response = httpAsyncClient.execute(HttpUtils.newHttpGet(dataImportUrl, importParams), null).get();
                        if (log.isInfoEnabled()) {
                            log.info("SolrDeltaImport : Send Solr Core :{},entityName :{}, responseStatus :{},response Data:{}",
                                    solrCore, entity,
                                    response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity(), Consts.UTF_8));
                        }
                    }
                }
            }
        }
    }
}

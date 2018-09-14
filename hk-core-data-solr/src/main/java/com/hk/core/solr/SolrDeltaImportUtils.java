package com.hk.core.solr;

import com.hk.commons.http.HttpExecutor;
import com.hk.commons.http.get.SimpleGetHttpExecutor;
import com.hk.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Solr 增量导入
 *
 * @author: kevin
 * @date: 2018-07-04 12:40
 */
public abstract class SolrDeltaImportUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(SolrDeltaImportUtils.class);

    /**
     * @param method   方法
     * @param hostUrl  solr host
     * @param solrCore solrCore
     * @throws IOException
     */
    public static void simpleGetDeltaImport(Method method,
                                            String hostUrl, String solrCore) {
        deltaImport(new SimpleGetHttpExecutor(), method, hostUrl, solrCore);
    }

    /**
     * @param executor execoutor
     * @param method   method
     * @param hostUrl  solr host
     * @param solrCore solrCore
     * @throws IOException
     */
    public static void deltaImport(HttpExecutor<String, Map<String, Object>> executor, Method method,
                                   String hostUrl, String solrCore) {
        SolrDeltaImport solrDeltaImport = AnnotationUtils.getAnnotation(method,
                SolrDeltaImport.class);
        if(null != solrDeltaImport){
            String[] entities = solrDeltaImport.entities();
            Arrays.stream(entities).forEach(item -> {
                try {
                    String response = executor.execute(getDataImportUrl(hostUrl, solrCore), getDeltaImportParams(item));
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Solr response result :{}", response);
                    }
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            });
        }
    }

    /**
     * @param entity
     * @return
     */
    private static Map<String, Object> getDeltaImportParams(String entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("verbose", "false");
        params.put("command", "delta-import");
        params.put("clean", "false");
        params.put("commit", "true");
        if (StringUtils.isNotBlank(entity)) {
            params.put("entity", entity);
        }
        return params;
    }

    /**
     * @param hostUrl
     * @param solrCore
     * @return
     */
    private static String getDataImportUrl(String hostUrl, String solrCore) {
        if (!StringUtils.endsWith(hostUrl, "/")) {
            hostUrl += "/";
        }
        return hostUrl + solrCore + "/dataimport";
    }
}

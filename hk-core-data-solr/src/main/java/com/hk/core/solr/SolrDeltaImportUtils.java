package com.hk.core.solr;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.hk.commons.http.HttpExecutor;
import com.hk.commons.http.get.SimpleGetHttpExecutor;
import com.hk.commons.util.StringUtils;

/**
 * Solr 增量导入
 *
 * @author kevin
 * @date 2018-07-04 12:40
 */
public abstract class SolrDeltaImportUtils {

    /**
     * @param hostUrl  solr host
     * @param solrCore solr solrCore
     * @param entity   entity
     * @throws RuntimeException RuntimeException
     */
    public static String simpleGetDeltaImport(String hostUrl, String solrCore, String entity) {
        return deltaImport(new SimpleGetHttpExecutor(), hostUrl, solrCore, entity);
    }

    /**
     * @param executor execoutor
     * @param hostUrl  solr host
     * @param solrCore solrCore
     * @param entity   solr entity
     * @throws RuntimeException RuntimeException
     */
    public static <T> T deltaImport(HttpExecutor<T, Map<String, Object>> executor,
                                    String hostUrl, String solrCore, String entity) {
        try {
            return executor.execute(getDataImportUrl(hostUrl, solrCore), getDeltaImportParams(entity));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * @param entity entity
     * @return P
     */
    private static Map<String, Object> getDeltaImportParams(String entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("verbose", "false");
        params.put("command", "delta-import");
        params.put("clean", "false");
        params.put("commit", "true");
        if (StringUtils.isNotEmpty(entity)) {
            params.put("entity", entity);
        }
        return params;
    }

    /**
     * @param hostUrl  hostUrl
     * @param solrCore solrCore
     * @return solr dataImportUrl
     */
    private static String getDataImportUrl(String hostUrl, String solrCore) {
        if (!StringUtils.endsWith(hostUrl, "/")) {
            hostUrl += "/";
        }
        return hostUrl + solrCore + "/dataimport";
    }
}

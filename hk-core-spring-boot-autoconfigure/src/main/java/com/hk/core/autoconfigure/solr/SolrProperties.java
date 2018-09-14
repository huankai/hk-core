package com.hk.core.autoconfigure.solr;

/**
 * @author: kevin
 * @date: 2018-07-04 12:22
 */
public class SolrProperties extends org.springframework.boot.autoconfigure.solr.SolrProperties {

    private String solrCore;

    public String getSolrCore() {
        return solrCore;
    }

    public void setSolrCore(String solrCore) {
        this.solrCore = solrCore;
    }
}

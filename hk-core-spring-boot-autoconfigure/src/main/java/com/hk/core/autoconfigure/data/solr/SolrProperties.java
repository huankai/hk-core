package com.hk.core.autoconfigure.data.solr;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kevin
 * @date 2018-07-04 12:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SolrProperties extends org.springframework.boot.autoconfigure.solr.SolrProperties {

    private String solrCore;

    /**
     * 是否使用Delta 导入，默认为false
     */
    protected boolean enableDeltaImport;

}

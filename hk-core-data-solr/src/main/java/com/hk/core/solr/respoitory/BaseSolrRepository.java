package com.hk.core.solr.respoitory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

/**
 * @author: kevin
 * @date: 2018-07-04 13:01
 */
public abstract class BaseSolrRepository {

    @Autowired
    protected SolrTemplate solrTemplate;


}

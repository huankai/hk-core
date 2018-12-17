package com.hk.core.solr.query;

import org.springframework.data.solr.core.query.Criteria;

/**
 * @author sjq-278
 * @date 2018-12-03 16:48
 */
public interface Condition {

    Criteria toSolrCriteria();
}

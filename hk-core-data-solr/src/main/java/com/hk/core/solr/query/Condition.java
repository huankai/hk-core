package com.hk.core.solr.query;

import com.hk.commons.util.CollectionUtils;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SolrDataQuery;

import java.util.List;
import java.util.Objects;

/**
 * @author sjq-278
 * @date 2018-12-03 16:48
 */
public interface Condition {

    Criteria toSolrCriteria();

    static void addCriteria(SolrDataQuery query, List<Condition> conditions) {
        if (CollectionUtils.isNotEmpty(conditions)) {
            conditions.forEach(item -> {
                Criteria criteria = item.toSolrCriteria();
                if (Objects.nonNull(criteria)) {
                    query.addCriteria(criteria);
                }
            });
        }
    }
}

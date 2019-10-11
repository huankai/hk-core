package com.hk.core.elasticsearch.query;

import com.hk.commons.util.CollectionUtils;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.util.List;
import java.util.Objects;

public interface Condition {

    Criteria toElasticsearchCriteria();

    static void addCriteria(CriteriaQuery query, List<Condition> conditions) {
        if (CollectionUtils.isNotEmpty(conditions)) {
            conditions.forEach(item -> {
                Criteria criteria = item.toElasticsearchCriteria();
                if (Objects.nonNull(criteria)) {
                    query.addCriteria(criteria);
                }
            });
        }
    }

}

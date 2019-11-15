package com.hk.core.data.jpa.query.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Deprecated
@AllArgsConstructor
public class AggregateExpression implements Criterion {

    @Getter
    private Projection projection;

    @Override
    @SuppressWarnings({"rawtypes"})
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        projection.toExpression(root, cq, cb);
        return null;
    }

}

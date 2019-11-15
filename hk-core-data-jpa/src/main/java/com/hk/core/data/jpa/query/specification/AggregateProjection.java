package com.hk.core.data.jpa.query.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.criteria.*;

@Deprecated
@AllArgsConstructor
public class AggregateProjection implements Projection {

    @Getter
    private String propertyName;

    @Getter
    private Aggregate aggregate;

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Expression<?> toExpression(Root<?> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Path path = PathUtils.getPath(root, propertyName);
        switch (this.aggregate) {
            case MAX:
                return cb.max(path);
            case MIN:
                return cb.min(path);
            case AVG:
                return cb.avg(path);
            case SUM:
                return cb.sum(path);
            default:
                return null;
        }
    }
}

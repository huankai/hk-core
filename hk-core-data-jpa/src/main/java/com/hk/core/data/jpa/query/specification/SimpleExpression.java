package com.hk.core.data.jpa.query.specification;

import com.hk.core.data.commons.query.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;
import java.util.Collection;
import java.util.Iterator;


/**
 *
 */
@AllArgsConstructor
public class SimpleExpression implements Criterion {

    @Getter
    private String propertyName;

    @Getter
    private Operator operator;

    @Getter
    private Object value;

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Path path = PathUtils.getPath(root, propertyName);
        switch (this.operator) {
            case EQ:
                return cb.equal(path, getValue());
            case NE:
                return cb.notEqual(path, getValue());
            case LIKE:
                return cb.like(path, (String) getValue());
            case LT:
                return cb.lessThan(path, (Comparable) getValue());
            case GT:
                return cb.greaterThan(path, (Comparable) getValue());
            case LTE:
                return cb.lessThanOrEqualTo(path, (Comparable) getValue());
            case GTE:
                return cb.greaterThanOrEqualTo(path, (Comparable) getValue());
            case IN:
                if (!(value instanceof Collection)) {
                    return null;
                }
                In<Object> in = cb.in(path);
                Collection<Object> c = (Collection) value;
                Object o;
                for (Iterator it = c.iterator(); it.hasNext(); in = in.value(o)) {
                    o = it.next();
                }
                return in;
            case BETWEEN:
                if (this.value instanceof Comparable[]) {
                    Comparable[] values = (Comparable[]) value;
                    if (values.length == 2) {
                        return cb.between(path, values[0], values[1]);
                    }
                }
                return null;
            case ISNULL:
                return cb.isNull(path);
            case ISNOTNULL:
                return cb.isNotNull(path);
            default:
        }
        return null;
    }

}

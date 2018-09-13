package com.hk.core.data.jpa.query.specification;

import com.hk.core.data.commons.query.Operator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Iterator;

public class AggregateExpression implements Criterion {

    private Projection projection;

    private Object value;

    private Operator operator;

    public AggregateExpression(Projection projection, Object value, Operator operator) {
        this.projection = projection;
        this.value = value;
        this.operator = operator;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        switch (operator) {
            case EQ:
                return cb.equal(projection.toExpression(root, cq, cb), value);
            case NE:
                return cb.notEqual(projection.toExpression(root, cq, cb), value);
            case LIKE:
                return cb.like(projection.toExpression(root, cq, cb), (String) value);
            case LT:
                return cb.lessThan(projection.toExpression(root, cq, cb), (Comparable) value);
            case GT:
                return cb.greaterThan(projection.toExpression(root, cq, cb), (Comparable) value);
            case LTE:
                return cb.lessThanOrEqualTo(projection.toExpression(root, cq, cb), (Comparable) value);
            case GTE:
                return cb.greaterThanOrEqualTo(projection.toExpression(root, cq, cb), (Comparable) value);
            case IN:
                if (!(value instanceof Collection)) {
                    return null;
                }
                In<Object> in = cb.in(projection.toExpression(root, cq, cb));
                Collection<Object> c = (Collection) value;
                Object o;
                for (Iterator it = c.iterator(); it.hasNext(); in = in.value(o)) {
                    o = it.next();
                }
                return in;
            case BETWEEN:
                if (value instanceof Comparable[]) {
                    Comparable[] values = ((Comparable[]) value);
                    if (values.length == 2) {
                        return cb.between(projection.toExpression(root, cq, cb), values[0], values[1]);
                    }
                }
                return null;
            case ISNULL:
                return cb.isNull(projection.toExpression(root, cq, cb));
            case ISNOTNULL:
                return cb.isNotNull(projection.toExpression(root, cq, cb));
            default:
                return null;
        }
    }

}

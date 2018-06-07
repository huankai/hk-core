package com.hk.core.data.jpa.query.specification;

import com.hk.core.data.commons.query.AndOr;
import com.hk.core.data.commons.query.MatchMode;
import com.hk.core.data.commons.query.Operator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * Created by chenzhen on 2016/2/24.
 * JPA条件生成工具
 */
public class Restrictions {

    public static SimpleExpression eq(String propertyName, Object value) {
        return StringUtils.isEmpty(value) ? null : new SimpleExpression(propertyName, Operator.EQ, value);
    }

    public static SimpleExpression ne(String propertyName, Object value) {
        return StringUtils.isEmpty(value) ? null : new SimpleExpression(propertyName, Operator.NE, value);
    }

    public static SimpleExpression like(String propertyName, String value) {
        return StringUtils.isEmpty(value) ? null : new SimpleExpression(propertyName, Operator.LIKE, MatchMode.ANYWHERE.toMatchString(value));
    }

    public static SimpleExpression like(String propertyName, String pattern, MatchMode matchMode) {
        return StringUtils.isEmpty(pattern) ? null : new SimpleExpression(propertyName, Operator.LIKE, matchMode.toMatchString(pattern));
    }

    public static SimpleExpression gt(String propertyName, Comparable value) {
        return StringUtils.isEmpty(value) ? null : new SimpleExpression(propertyName, Operator.GT, value);
    }

    public static SimpleExpression lt(String propertyName, Comparable value) {
        return StringUtils.isEmpty(value) ? null : new SimpleExpression(propertyName, Operator.LT, value);
    }

    public static SimpleExpression lte(String propertyName, Comparable value) {
        return StringUtils.isEmpty(value) ? null : new SimpleExpression(propertyName, Operator.LTE, value);
    }

    public static SimpleExpression gte(String propertyName, Comparable value) {
        return StringUtils.isEmpty(value) ? null : new SimpleExpression(propertyName, Operator.GTE, value);
    }

    public static SimpleExpression in(String propertyName, Collection value) {
        return CollectionUtils.isEmpty(value) ? null : new SimpleExpression(propertyName, Operator.IN, value);
    }

    public static SimpleExpression between(String propertyName, Comparable value1, Comparable value2) {
        Comparable[] values = new Comparable[2];
        values[0] = value1;
        values[1] = value2;
        return (StringUtils.isEmpty(value1) || StringUtils.isEmpty(value2)) ? null : new SimpleExpression(propertyName, Operator.BETWEEN, values);
    }

    public static SimpleExpression isNull(String propertyName) {
        return new SimpleExpression(propertyName, Operator.ISNULL, null);
    }

    public static SimpleExpression isNotNull(String propertyName) {
        return new SimpleExpression(propertyName, Operator.ISNOTNULL, null);
    }

    public static AggregateExpression eq(Projection projection, Object value) {
        return StringUtils.isEmpty(value) ? null : new AggregateExpression(projection, value, Operator.EQ);
    }

    public static AggregateExpression ne(Projection projection, Object value) {
        return StringUtils.isEmpty(value) ? null : new AggregateExpression(projection, value, Operator.NE);
    }

    public static AggregateExpression gt(Projection projection, Comparable value) {
        return StringUtils.isEmpty(value) ? null : new AggregateExpression(projection, value, Operator.GT);
    }

    public static AggregateExpression lt(Projection projection, Comparable value) {
        return StringUtils.isEmpty(value) ? null : new AggregateExpression(projection, value, Operator.LT);
    }

    public static AggregateExpression gte(Projection projection, Comparable value) {
        return StringUtils.isEmpty(value) ? null : new AggregateExpression(projection, value, Operator.GTE);
    }

    public static AggregateExpression lte(Projection projection, Comparable value) {
        return StringUtils.isEmpty(value) ? null : new AggregateExpression(projection, value, Operator.LTE);
    }

    public static AggregateExpression in(Projection projection, Collection value) {
        return CollectionUtils.isEmpty(value) ? null : new AggregateExpression(projection, value, Operator.IN);
    }

    public static AggregateExpression between(Projection projection, Comparable value1, Comparable value2) {
        Comparable[] values = new Comparable[2];
        values[0] = value1;
        values[1] = value2;
        return (StringUtils.isEmpty(value1) || StringUtils.isEmpty(value2)) ? null : new AggregateExpression(projection, values, Operator.BETWEEN);
    }

    public static AggregateExpression isNull(Projection projection) {
        return new AggregateExpression(projection, null, Operator.ISNULL);
    }

    public static AggregateExpression isNotNull(Projection projection) {
        return new AggregateExpression(projection, null, Operator.ISNOTNULL);
    }

    public static LogicalExpression and(Criterion... criterions) {
        return criterions.length == 0 ? null : new LogicalExpression(criterions, AndOr.AND);
    }

    public static LogicalExpression or(Criterion... criterions) {
        return criterions.length == 0 ? null : new LogicalExpression(criterions, AndOr.OR);
    }

}

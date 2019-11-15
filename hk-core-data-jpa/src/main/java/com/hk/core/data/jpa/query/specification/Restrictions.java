package com.hk.core.data.jpa.query.specification;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.ObjectUtils;
import com.hk.core.data.commons.query.AndOr;
import com.hk.core.data.commons.query.MatchMode;
import com.hk.core.data.commons.query.Operator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * @author kally
 * @date 2017年8月6日上午9:05:57
 */
public class Restrictions {

    /**
     * @param propertyName propertyName
     * @param value        value
     * @return SimpleExpression
     */
    public static SimpleExpression eq(String propertyName, Object value) {
        return ObjectUtils.isEmpty(value) ? null : new SimpleExpression(propertyName, Operator.EQ, value);
    }

    /**
     * @param propertyName propertyName
     * @param value        value
     * @return SimpleExpression
     */
    public static SimpleExpression ne(String propertyName, Object value) {
        return ObjectUtils.isEmpty(value) ? null : new SimpleExpression(propertyName, Operator.NE, value);
    }

    /**
     * @param propertyName propertyName
     * @param value        value
     * @return SimpleExpression
     */
    public static SimpleExpression like(String propertyName, String value) {
        return StringUtils.isEmpty(value) ? null
                : new SimpleExpression(propertyName, Operator.LIKE, MatchMode.ANYWHERE.toMatchString(value));
    }

    /**
     * @param propertyName propertyName
     * @param pattern      pattern
     * @param matchMode    matchMode
     * @return SimpleExpression
     */
    public static SimpleExpression like(String propertyName, String pattern, MatchMode matchMode) {
        return StringUtils.isEmpty(pattern) ? null
                : new SimpleExpression(propertyName, Operator.LIKE, matchMode.toMatchString(pattern));
    }

    /**
     * @param propertyName propertyName
     * @param value        value
     * @return SimpleExpression
     */
    public static SimpleExpression gt(String propertyName, Comparable<?> value) {
        return Objects.isNull(value) ? null : new SimpleExpression(propertyName, Operator.GT, value);
    }

    /**
     * @param propertyName propertyName
     * @param value        value
     * @return SimpleExpression
     */
    public static SimpleExpression lt(String propertyName, Comparable<?> value) {
        return Objects.isNull(value) ? null : new SimpleExpression(propertyName, Operator.LT, value);
    }

    /**
     * @param propertyName propertyName
     * @param value        value
     * @return SimpleExpression
     */
    public static SimpleExpression lte(String propertyName, Comparable<?> value) {
        return Objects.isNull(value) ? null : new SimpleExpression(propertyName, Operator.LTE, value);
    }

    /**
     * @param propertyName propertyName
     * @param value        value
     * @return SimpleExpression
     */
    public static SimpleExpression gte(String propertyName, Comparable<?> value) {
        return Objects.isNull(value) ? null : new SimpleExpression(propertyName, Operator.GTE, value);
    }

    /**
     * @param propertyName propertyName
     * @param value        value
     * @return SimpleExpression
     */
    public static SimpleExpression in(String propertyName, Collection<?> value) {
        return CollectionUtils.isEmpty(value) ? null : new SimpleExpression(propertyName, Operator.IN, value);
    }

    /**
     * @param propertyName propertyName
     * @param value1       value1
     * @param value2       value2
     * @return SimpleExpression
     */
    public static SimpleExpression between(String propertyName, Comparable<?> value1, Comparable<?> value2) {
        return (Objects.isNull(value1) || Objects.isNull(value2)) ? null
                : new SimpleExpression(propertyName, Operator.BETWEEN, new Comparable[]{value1, value2});
    }

    /**
     * @param propertyName propertyName
     * @return SimpleExpression
     */
    public static SimpleExpression isNull(String propertyName) {
        return new SimpleExpression(propertyName, Operator.ISNULL, null);
    }

    /**
     * @param propertyName propertyName
     * @return SimpleExpression
     */
    public static SimpleExpression isNotNull(String propertyName) {
        return new SimpleExpression(propertyName, Operator.ISNOTNULL, null);
    }
    /**
     * @param criterions projection
     * @return LogicalExpression
     */
    public static LogicalExpression and(Criterion... criterions) {
        return ArrayUtils.isEmpty(criterions) ? null : new LogicalExpression(AndOr.AND, criterions);
    }

    /**
     * @param criterions projection
     * @return LogicalExpression
     */
    public static LogicalExpression or(Criterion... criterions) {
        return ArrayUtils.isEmpty(criterions) ? null : new LogicalExpression(AndOr.OR, criterions);
    }

}

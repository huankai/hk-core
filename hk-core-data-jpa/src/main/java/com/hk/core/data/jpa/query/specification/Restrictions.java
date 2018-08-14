package com.hk.core.data.jpa.query.specification;

import java.util.Collection;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.hk.commons.util.ArrayUtils;
import com.hk.core.data.commons.query.AndOr;
import com.hk.core.data.commons.query.MatchMode;
import com.hk.core.data.commons.query.Operator;

/**
 * 
 * @author kally
 * @date 2017年8月6日上午9:05:57
 */
public class Restrictions {

	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static SimpleExpression<?> eq(String propertyName, Object value) {
		return StringUtils.isEmpty(value) ? null : new SimpleExpression<>(propertyName, Operator.EQ, value);
	}

	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static SimpleExpression<?> ne(String propertyName, Object value) {
		return StringUtils.isEmpty(value) ? null : new SimpleExpression<>(propertyName, Operator.NE, value);
	}

	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static SimpleExpression<?> like(String propertyName, String value) {
		return StringUtils.isEmpty(value) ? null
				: new SimpleExpression<>(propertyName, Operator.LIKE, MatchMode.ANYWHERE.toMatchString(value));
	}

	/**
	 * 
	 * @param propertyName
	 * @param pattern
	 * @param matchMode
	 * @return
	 */
	public static SimpleExpression<?> like(String propertyName, String pattern, MatchMode matchMode) {
		return StringUtils.isEmpty(pattern) ? null
				: new SimpleExpression<>(propertyName, Operator.LIKE, matchMode.toMatchString(pattern));
	}

	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static SimpleExpression<?> gt(String propertyName, Comparable<?> value) {
		return StringUtils.isEmpty(value) ? null : new SimpleExpression<>(propertyName, Operator.GT, value);
	}

	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static SimpleExpression<?> lt(String propertyName, Comparable<?> value) {
		return StringUtils.isEmpty(value) ? null : new SimpleExpression<>(propertyName, Operator.LT, value);
	}

	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static SimpleExpression<?> lte(String propertyName, Comparable<?> value) {
		return StringUtils.isEmpty(value) ? null : new SimpleExpression<>(propertyName, Operator.LTE, value);
	}

	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static SimpleExpression<?> gte(String propertyName, Comparable<?> value) {
		return StringUtils.isEmpty(value) ? null : new SimpleExpression<>(propertyName, Operator.GTE, value);
	}

	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static SimpleExpression<?> in(String propertyName, Collection<?> value) {
		return CollectionUtils.isEmpty(value) ? null : new SimpleExpression<>(propertyName, Operator.IN, value);
	}

	/**
	 * 
	 * @param propertyName
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static SimpleExpression<?> between(String propertyName, Comparable<?> value1, Comparable<?> value2) {
		return (StringUtils.isEmpty(value1) || StringUtils.isEmpty(value2)) ? null
				: new SimpleExpression<>(propertyName, Operator.BETWEEN, new Comparable[] { value1, value2 });
	}

	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public static SimpleExpression<?> isNull(String propertyName) {
		return new SimpleExpression<>(propertyName, Operator.ISNULL, null);
	}

	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public static SimpleExpression<?> isNotNull(String propertyName) {
		return new SimpleExpression<>(propertyName, Operator.ISNOTNULL, null);
	}

	/**
	 * 
	 * @param projection
	 * @param value
	 * @return
	 */
	public static AggregateExpression<?> eq(Projection projection, Object value) {
		return StringUtils.isEmpty(value) ? null : new AggregateExpression<>(projection, value, Operator.EQ);
	}

	/**
	 * 
	 * @param projection
	 * @param value
	 * @return
	 */
	public static AggregateExpression<?> ne(Projection projection, Object value) {
		return StringUtils.isEmpty(value) ? null : new AggregateExpression<>(projection, value, Operator.NE);
	}

	/**
	 * 
	 * @param projection
	 * @param value
	 * @return
	 */
	public static AggregateExpression<?> gt(Projection projection, Comparable<?> value) {
		return StringUtils.isEmpty(value) ? null : new AggregateExpression<>(projection, value, Operator.GT);
	}

	/**
	 * 
	 * @param projection
	 * @param value
	 * @return
	 */
	public static AggregateExpression<?> lt(Projection projection, Comparable<?> value) {
		return StringUtils.isEmpty(value) ? null : new AggregateExpression<>(projection, value, Operator.LT);
	}

	/**
	 * 
	 * @param projection
	 * @param value
	 * @return
	 */
	public static AggregateExpression<?> gte(Projection projection, Comparable<?> value) {
		return StringUtils.isEmpty(value) ? null : new AggregateExpression<>(projection, value, Operator.GTE);
	}

	/**
	 * 
	 * @param projection
	 * @param value
	 * @return
	 */
	public static AggregateExpression<?> lte(Projection projection, Comparable<?> value) {
		return StringUtils.isEmpty(value) ? null : new AggregateExpression<>(projection, value, Operator.LTE);
	}

	/**
	 * 
	 * @param projection
	 * @param value
	 * @return
	 */
	public static AggregateExpression<?> in(Projection projection, Collection<?> value) {
		return CollectionUtils.isEmpty(value) ? null : new AggregateExpression<>(projection, value, Operator.IN);
	}

	/**
	 * 
	 * @param projection
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static AggregateExpression<?> between(Projection projection, Comparable<?> value1, Comparable<?> value2) {
		return (StringUtils.isEmpty(value1) || StringUtils.isEmpty(value2)) ? null
				: new AggregateExpression<>(projection, new Comparable[] { value1, value2 }, Operator.BETWEEN);
	}

	/**
	 * 
	 * @param projection
	 * @return
	 */
	public static AggregateExpression<?> isNull(Projection projection) {
		return new AggregateExpression<>(projection, null, Operator.ISNULL);
	}

	/**
	 * 
	 * @param projection
	 * @return
	 */
	public static AggregateExpression<?> isNotNull(Projection projection) {
		return new AggregateExpression<>(projection, null, Operator.ISNOTNULL);
	}

	/**
	 * 
	 * @param criterions
	 * @return
	 */
	public static LogicalExpression<?> and(@SuppressWarnings("unchecked") Criterion<Object>... criterions) {
		return ArrayUtils.isEmpty(criterions) ? null : new LogicalExpression<>(criterions, AndOr.AND);
	}

	/**
	 * 
	 * @param criterions
	 * @return
	 */
	public static LogicalExpression<?> or(@SuppressWarnings("unchecked") Criterion<Object>... criterions) {
		return ArrayUtils.isEmpty(criterions) ? null : new LogicalExpression<>(criterions, AndOr.OR);
	}

}

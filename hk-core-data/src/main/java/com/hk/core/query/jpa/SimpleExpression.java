package com.hk.core.query.jpa;

import java.util.Collection;
import java.util.Iterator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.hk.core.query.Operator;

public class SimpleExpression<T> implements Criterion<T> {

	private String propertyName;

	private Operator operator;

	private Object value;

	public SimpleExpression(String propertyName, Object value) {
		this(propertyName, Operator.EQ, value);
	}

	public SimpleExpression(String propertyName, Operator operator, Object value) {
		this.propertyName = propertyName;
		this.operator = operator;
		this.value = value;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate toPredicate(Root<T> root, CriteriaQuery<T> cq, CriteriaBuilder cb) {
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

	public String getPropertyName() {
		return propertyName;
	}

	public Operator getOperator() {
		return operator;
	}

	public Object getValue() {
		return value;
	}

}

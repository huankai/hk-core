package com.hk.core.data.jpa.query.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public class AggregateProjection implements Projection {

	private String propertyName;

	private Aggregate aggregate;

	public AggregateProjection(String propertyName, Aggregate aggregate) {
		this.propertyName = propertyName;
		this.aggregate = aggregate;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
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

	public String getPropertyName() {
		return propertyName;
	}

	public Aggregate getAggregate() {
		return aggregate;
	}

}

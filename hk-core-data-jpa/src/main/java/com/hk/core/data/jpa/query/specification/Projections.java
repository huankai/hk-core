package com.hk.core.data.jpa.query.specification;

public class Projections {

	public static AggregateProjection max(String propertyName) {
		return new AggregateProjection(propertyName, Aggregate.MAX);
	}

	public static AggregateProjection min(String propertyName) {
		return new AggregateProjection(propertyName, Aggregate.MIN);
	}

	public static AggregateProjection avg(String propertyName) {
		return new AggregateProjection(propertyName, Aggregate.AVG);
	}

	public static AggregateProjection sum(String propertyName) {
		return new AggregateProjection(propertyName, Aggregate.SUM);
	}

}

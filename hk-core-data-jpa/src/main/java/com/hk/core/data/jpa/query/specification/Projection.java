package com.hk.core.data.jpa.query.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

public interface Projection {

    @SuppressWarnings("rawtypes")
	Expression toExpression(Root<?> root, CriteriaQuery<?> cq, CriteriaBuilder cb);

}

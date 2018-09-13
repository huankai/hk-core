package com.hk.core.data.jpa.query.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface Criterion {
	
	Predicate toPredicate(Root<?> root, CriteriaQuery<?> cq, CriteriaBuilder cb);

}

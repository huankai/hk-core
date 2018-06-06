package com.hk.core.query.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface Criterion<T> {
	
	Predicate toPredicate(Root<T> root, CriteriaQuery<T> cq, CriteriaBuilder cb);

}

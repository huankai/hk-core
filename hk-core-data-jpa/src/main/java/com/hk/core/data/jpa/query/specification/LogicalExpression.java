package com.hk.core.data.jpa.query.specification;

import com.google.common.collect.Lists;
import com.hk.core.data.commons.query.AndOr;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class LogicalExpression<T> implements Criterion<T> {

	private Criterion<T>[] criterions;

	private AndOr andOr;

	public LogicalExpression(Criterion<T>[] criterions, AndOr andOr) {
		this.criterions = criterions;
		this.andOr = andOr;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<T> cq, CriteriaBuilder cb) {
		List<Predicate> predicates = Lists.newArrayList();
		Criterion<T>[] crits = criterions;
		for (int index = 0; index < crits.length; ++index) {
			Criterion<T> criterion = crits[index];
			if (criterion != null) {
				predicates.add(criterion.toPredicate(root, cq, cb));
			}
		}
		if (predicates.size() == 0) {
			return null;
		} else {
			switch (andOr) {
			case OR:
				return cb.or((Predicate[]) predicates.toArray(new Predicate[0]));
			default:
				return cb.and((Predicate[]) predicates.toArray(new Predicate[0]));
			}
		}
	}

	public Criterion<T>[] getCriterions() {
		return criterions;
	}

	public AndOr getAndOr() {
		return andOr;
	}
}

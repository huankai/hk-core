package com.hk.core.data.jpa.query.specification;

import com.hk.core.data.commons.query.AndOr;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class LogicalExpression implements Criterion {

	private Criterion[] criterions;

	private AndOr andOr;

	public LogicalExpression(Criterion[] criterions, AndOr andOr) {
		this.criterions = criterions;
		this.andOr = andOr;
	}

	@Override
	public Predicate toPredicate(Root<?> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		Criterion[] crits = criterions;
		for (int index = 0; index < crits.length; ++index) {
			Criterion criterion = crits[index];
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

	public Criterion[] getCriterions() {
		return criterions;
	}

	public AndOr getAndOr() {
		return andOr;
	}
}

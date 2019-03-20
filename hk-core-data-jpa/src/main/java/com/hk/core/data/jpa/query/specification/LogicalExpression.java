package com.hk.core.data.jpa.query.specification;

import com.hk.commons.util.ArrayUtils;
import com.hk.core.data.commons.query.AndOr;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class LogicalExpression implements Criterion {

    @Getter
    private AndOr andOr;

    @Getter
    private Criterion[] criterions;

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        if (ArrayUtils.isEmpty(this.criterions)) {
            return null;
        }
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
            if (andOr == AndOr.OR) {
                return cb.or((Predicate[]) predicates.toArray(new Predicate[0]));
            }
            return cb.and((Predicate[]) predicates.toArray(new Predicate[0]));
        }
    }
}

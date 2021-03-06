package com.hk.core.data.jpa.query.specification;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.query.Order;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked", "serial"})
public class Criteria<T> implements Specification<T> {

    @Getter
    private List<Criterion> criterions = new ArrayList<>();

    @Getter
    private List<Criterion> havings = new ArrayList<>();

    @Getter
    private List<Order> orders = new ArrayList<>();

    @Getter
    private List<String> groupByPropertyNames;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List predicates;
        Iterator iterator;
        Criterion criterion;
        if (!this.criterions.isEmpty()) {
            predicates = new ArrayList();
            iterator = this.criterions.iterator();
            while (iterator.hasNext()) {
                criterion = (Criterion) iterator.next();
                if (criterion != null) {
                    Predicate predicate = criterion.toPredicate(root, query, cb);
                    if (predicate != null) {
                        predicates.add(predicate);
                    }
                }
            }
            if (predicates.size() > 0) {
                query.where(cb.and((Predicate[]) predicates.toArray(new Predicate[0])));
            }
        }
        if (this.orders != null) {
            predicates = new ArrayList();
            iterator = this.orders.iterator();
            while (iterator.hasNext()) {
                Order order = (Order) iterator.next();
                predicates.add(OrderUtils.toJpaOrder(root, order));
            }
            query.orderBy(predicates);
        }
        if (this.groupByPropertyNames != null) {
            predicates = new ArrayList();
            iterator = this.groupByPropertyNames.iterator();

            while (iterator.hasNext()) {
                String propertyName = (String) iterator.next();
                predicates.add(PathUtils.getPath(root, propertyName));
            }

            query.groupBy(predicates);
        }
        if (!this.havings.isEmpty()) {
            predicates = new ArrayList();
            iterator = this.havings.iterator();
            while (iterator.hasNext()) {
                criterion = (Criterion) iterator.next();
                predicates.add(criterion.toPredicate(root, query, cb));
            }
            if (predicates.size() > 0) {
                query.having((Predicate[]) predicates.toArray(new Predicate[0]));
            }
        }
        return null;
    }

    public Criteria<T> add(Criterion criterion) {
        if (criterion != null) {
            this.criterions.add(criterion);
        }
        return this;
    }

    public Criteria<T> having(Criterion criterion) {
        if (criterion != null) {
            this.havings.add(criterion);
        }
        return this;
    }

    public void addOrder(Order... orders) {
        CollectionUtils.addAllNotNull(this.orders, orders);
    }

    public void addGroupBy(List<String> propertyNames) {
        this.groupByPropertyNames = propertyNames;
    }

    public void addGroupBy(String... propertyNames) {
        if (ArrayUtils.isNotEmpty(propertyNames)) {
            addGroupBy(Arrays.asList(propertyNames));
        }
    }
}

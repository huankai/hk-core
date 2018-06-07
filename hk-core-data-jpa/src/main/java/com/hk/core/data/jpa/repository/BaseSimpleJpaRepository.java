package com.hk.core.data.jpa.repository;

import com.google.common.collect.Lists;
import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.data.commons.query.Order;
import com.hk.core.data.commons.query.QueryModel;
import com.hk.core.data.commons.query.QueryPage;
import com.hk.core.data.commons.query.SimpleQueryPage;
import com.hk.core.data.jpa.query.JpaQueryModel;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: huangkai
 * @date 2018-06-07 13:34
 */
public class BaseSimpleJpaRepository<T extends Persistable<ID>, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    public BaseSimpleJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public boolean deleteById(ID id) {
        delete(id);
        return true;
    }

    @Override
    public boolean insert(T t) {
        save(t);
        return true;
    }

    @Override
    public boolean batchInsert(Iterable<T> iterable) {
        save(iterable);
        return true;
    }

    @Override
    public T getById(ID id) {
        return getOne(id);
    }

    @Override
    public T findById(ID id) {
        return findOne(id);
    }

    @Override
    public boolean exists(T t) {
        return exists(Example.of(t, ofExampleMatcher()));
    }

    @Override
    public List<T> findAll(T t, Order... orders) {
        List<Order> orderList = Lists.newArrayList();
        if (ArrayUtils.isNotEmpty(orders)) {
            orderList.addAll(Arrays.asList(orders));
        }
        return findAll(Example.of(t, ofExampleMatcher()), getSort((orderList)));
    }

    @Override
    public Iterable<T> findByIds(Iterable<ID> ids) {
        return findAll(ids);
    }

    @Override
    public T findOne(T t) {
        return findOne(Example.of(t, ofExampleMatcher()));
    }

    @Override
    public long count(T t) {
        return count(Example.of(t, ofExampleMatcher()));
    }

    private Sort getSort(List<Order> orders) {
        List<Sort.Order> jpaOrders = orders.stream()
                .map(order -> new Sort.Order(order.isDesc() ? Sort.Direction.DESC : Sort.Direction.ASC, order.getField()))
                .collect(Collectors.toList());
        Sort sort = null;
        if (CollectionUtils.isNotEmpty(orders)) {
            sort = new Sort(jpaOrders);
        }
        return sort;
    }

    @Override
    @SuppressWarnings("unchecked")
    public QueryPage<T> findByPage(QueryModel query) {
        T param = null;
        if (query instanceof JpaQueryModel) {
            JpaQueryModel<T> queryModel = (JpaQueryModel<T>) query;
            param = queryModel.getParam();
            if (null == param) {
                param = BeanUtils.instantiate(getDomainClass());
            }
        }
        return findByPage(param, query.getOrders(), query.getStartRowIndex(), query.getPageSize());
    }

    @Override
    public QueryPage<T> findByPage(T t, List<Order> orders, int pageIndex, int pageSize) {
        Page<T> page = findAll(Example.of(t, ofExampleMatcher()), new PageRequest(pageIndex, pageSize, getSort(orders)));
        return new SimpleQueryPage<>(page.getContent(), page.getTotalElements(), pageIndex, pageSize);
    }

    @Override
    public final boolean updateByPrimaryKey(T t) {
        if (t.isNew()) {
            throw new RuntimeException("entity primary key must not be null");
        }
        save(t);
        return true;
    }

    @Override
    public final boolean updateByPrimaryKeySelective(T t) {
        if (t.isNew()) {
            throw new RuntimeException("entity primary key must not be null");
        }
        T find = findOne(t.getId());
        if (null == find) {
            throw new RuntimeException("");
        }
        //edit
        save(t);
        return true;
    }
}

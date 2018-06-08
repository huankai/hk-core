package com.hk.core.data.jpa.repository;

import com.google.common.collect.Lists;
import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.data.commons.query.Order;
import com.hk.core.data.commons.query.QueryPage;
import com.hk.core.data.commons.query.SimpleQueryPage;
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
    public T insert(T t) {
        return save(t);
    }

    @Override
    public Iterable<T> batchInsert(Iterable<T> iterable) {
        return save(iterable);
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
    public Iterable<T> insertOrUpdate(Iterable<T> entities) {
        return save(entities);
    }

    @Override
    public List<T> findAll(Example<T> example, Order... orders) {
        List<Order> orderList = Lists.newArrayList();
        if (ArrayUtils.isNotEmpty(orders)) {
            orderList.addAll(Arrays.asList(orders));
        }
        return findAll(example, getSort((orderList)));
    }

    @Override
    public Iterable<T> findByIds(Iterable<ID> ids) {
        return findAll(ids);
    }

    @Override
    public boolean deleteEntities(Iterable<T> entities) {
        deleteInBatch(entities);
        return true;
    }

    @Override
    public boolean deleteById(ID id) {
        delete(id);
        return true;
    }

    @Override
    public <S extends T> boolean deleteEntity(T t) {
        super.delete(t);
        return true;
    }

    @Override
    public QueryPage<T> findByPage(Example<T> example, List<Order> orders, int pageIndex, int pageSize) {
        Page<T> page = findAll(example, new PageRequest(pageIndex, pageSize, getSort(orders)));
        return new SimpleQueryPage<>(page.getContent(), page.getTotalElements(), pageIndex, pageSize);
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
    public final T updateById(T t) {
        AssertUtils.isTrue(!t.isNew(), "The given id must not be null!");
        return save(t);
    }

    @Override
    public final T updateByIdSelective(T t) {
        AssertUtils.isTrue(!t.isNew(), "The given id must not be null!");
        T find = getOne(t.getId());
        BeanUtils.copyNotNullProperties(t, find);
        return save(find);
    }
}

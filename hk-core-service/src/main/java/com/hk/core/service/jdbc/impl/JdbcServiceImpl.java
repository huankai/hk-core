package com.hk.core.service.jdbc.impl;

import com.hk.commons.util.ListResult;
import com.hk.core.data.jdbc.repository.JdbcRepository;
import com.hk.core.jdbc.SelectArguments;
import com.hk.core.jdbc.query.CompositeCondition;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.query.QueryModel;
import com.hk.core.service.impl.BaseServiceImpl;
import com.hk.core.service.jdbc.JdbcBaseService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

/**
 * @author kevin
 * @date 2018-10-11 14:37
 */
public abstract class JdbcServiceImpl<T extends Persistable<ID>, ID extends Serializable> extends BaseServiceImpl<T, ID> implements JdbcBaseService<T, ID> {

    @Override
    protected abstract JdbcRepository<T, ID> getBaseRepository();

    @Override
    public ListResult<T> findAll(T t, Order... orders) {
        return getBaseRepository().findAll(t, orders);
    }

    @Override
    public QueryPage<T> queryForPage(QueryModel<T> query) {
        return getBaseRepository().queryForPage(query);
    }

    @Override
    public QueryPage<T> queryForPage(SelectArguments selectArguments) {
        return getBaseRepository().queryForPage(selectArguments);
    }

    @Override
    public T getById(ID id) {
        return getBaseRepository().getById(id);
    }

    @Override
    public T getOne(T t) {
        return getBaseRepository().getOne(t);
    }

    @Override
    public Optional<T> findOne(T t) {
        return getBaseRepository().findOne(t);
    }

    @Override
    public Optional<T> findOne(CompositeCondition condition) {
        return getBaseRepository().findOne(condition);
    }

    @Override
    public T getOne(CompositeCondition condition) {
        return getBaseRepository().getOne(condition);
    }

    @Override
    public T updateByIdSelective(T t) {
        return getBaseRepository().updateByIdSelective(t);
    }

    @Override
    public long count(T t) {
        return getBaseRepository().count(t);
    }

    @Override
    public ListResult<T> findAll(CompositeCondition condition, Collection<String> groupBys, Order... orders) {
        return getBaseRepository().findAll(condition, groupBys, orders);
    }

    @Override
    public boolean delete(CompositeCondition conditions) {
        return getBaseRepository().delete(conditions);
    }
}

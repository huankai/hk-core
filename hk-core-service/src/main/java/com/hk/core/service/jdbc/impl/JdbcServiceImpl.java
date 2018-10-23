package com.hk.core.service.jdbc.impl;

import com.hk.core.data.jdbc.SelectArguments;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.data.jdbc.repository.JdbcRepository;
import com.hk.core.page.ListResult;
import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.service.impl.BaseServiceImpl;
import com.hk.core.service.jdbc.JdbcBaseService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Set;

/**
 * @author: sjq-278
 * @date: 2018-10-11 14:37
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
    public long count(T t) {
        return getBaseRepository().count(t);
    }

    @Override
    public ListResult<T> findAll(CompositeCondition condition, Set<String> groupBys, Order... orders) {
        return getBaseRepository().findAll(condition, groupBys, orders);
    }


}

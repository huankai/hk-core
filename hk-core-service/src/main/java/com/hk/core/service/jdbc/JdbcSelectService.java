package com.hk.core.service.jdbc;

import com.hk.core.data.jdbc.SelectArguments;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.page.ListResult;
import com.hk.core.query.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.service.SelectService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

/**
 * @author: kevin
 * @date: 2018-10-11 14:46
 */
public interface JdbcSelectService<T extends Persistable<ID>, ID extends Serializable> extends SelectService<T, ID> {


    /**
     * @param t      t
     * @param orders orders
     * @return List
     */
    ListResult<T> findAll(T t, Order... orders);

    T getById(ID id);


    /**
     * 查询唯一，如果查询有多条记录，只会返回第一条
     *
     * @param t t
     * @return T
     */
    Optional<T> findOne(T t);

    /**
     * 查询唯一，如果查询有多条记录，只会返回第一条
     *
     * @param condition condition
     * @return t
     */
    Optional<T> findOne(CompositeCondition condition);

    /**
     * @param query query
     * @return QueryPage
     */
    QueryPage<T> queryForPage(QueryModel<T> query);

    /**
     * 查询分页
     *
     * @param selectArguments selectArguments
     * @return QueryPage
     */
    QueryPage<T> queryForPage(SelectArguments selectArguments);

    /**
     * @param t t
     * @return count
     */
    long count(T t);

    default ListResult<T> findAll(CompositeCondition condition, Order... orders) {
        return findAll(condition, null, orders);
    }

    ListResult<T> findAll(CompositeCondition condition, Collection<String> groupBys, Order... orders);
}

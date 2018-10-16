package com.hk.core.service.jdbc;

import com.hk.core.data.jdbc.SelectArguments;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.page.ListResult;
import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.service.SelectService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Set;

/**
 * @author: sjq-278
 * @date: 2018-10-11 14:46
 */
public interface JdbcSelectService<T extends Persistable<ID>, ID extends Serializable> extends SelectService<T, ID> {


    /**
     * @param t      t
     * @param orders orders
     * @return List
     */
    ListResult<T> findAll(T t, Order... orders);

    /**
     * @param query query
     * @return QueryPage
     */
    QueryPage<T> queryForPage(QueryModel<T> query);

    QueryPage<T> queryForPage(SelectArguments selectArguments);

    /**
     * @param t t
     * @return count
     */
    long count(T t);

    default ListResult<T> findAll(CompositeCondition condition, Order... orders) {
        return findAll(condition, null, orders);
    }

    ListResult<T> findAll(CompositeCondition condition, Set<String> groupBys, Order... orders);
}

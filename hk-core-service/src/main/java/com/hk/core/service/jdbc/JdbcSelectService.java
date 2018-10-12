package com.hk.core.service.jdbc;

import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.service.SelectService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;

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
    List<T> findAll(T t, Order... orders);

    /**
     * @param query query
     * @return QueryPage
     */
    QueryPage<T> queryForPage(QueryModel<T> query);

    /**
     * @param t t
     * @return count
     */
    long count(T t);
}

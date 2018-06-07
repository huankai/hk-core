package com.hk.core.data.commons;

import com.hk.core.data.commons.query.Order;
import com.hk.core.data.commons.query.QueryModel;
import com.hk.core.data.commons.query.QueryPage;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangkai
 * @date 2018-6-6 22:50
 */
public interface Select<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * @param id
     * @return
     */
    T getById(ID id);

    /**
     * @param id
     * @return
     */
    T findById(ID id);

    /**
     * @param t
     * @param orders
     * @return
     */
    List<T> findAll(T t, Order... orders);

    /**
     * @param ids
     * @return
     */
    Iterable<T> findByIds(Iterable<ID> ids);

    /**
     * @return
     */
    List<T> findAll();

    /**
     * @param t
     * @return
     */
    T findOne(T t);

    /**
     * @return
     */
    long count();

    /**
     * @param t
     * @return
     */
    long count(T t);

    /**
     * 是否存在
     *
     * @param id
     * @return
     */
    boolean exists(ID id);

    /**
     * 是否存在
     *
     * @param t
     * @return
     */
    boolean exists(T t);

    /**
     * @param t
     * @param orders
     * @param pageIndex
     * @param pageSize
     * @return
     */
    QueryPage<T> findByPage(T t, List<Order> orders, int pageIndex, int pageSize);

    /**
     * @param query
     * @return
     */
    QueryPage<T> findByPage(QueryModel query);
}

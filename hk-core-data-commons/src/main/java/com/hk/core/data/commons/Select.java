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
     * @param t
     * @return
     */
    List<T> findAll(T t);

    /**
     * @param iterable
     * @return
     */
    List<T> findByIds(Iterable<ID> iterable);

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

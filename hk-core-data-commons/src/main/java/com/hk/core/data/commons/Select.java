package com.hk.core.data.commons;

import com.hk.core.data.commons.query.Order;
import com.hk.core.data.commons.query.QueryPage;
import org.springframework.data.domain.Example;
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
    List<T> findAll(Example<T> t, Order... orders);

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
    <S extends T> S findOne(Example<S> t);

    /**
     * @return
     */
    long count();

    /**
     * @param t
     * @return
     */
    <S extends T> long count(Example<S> t);

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
    <S extends T> boolean exists(Example<S> t);

    /**
     * @param t
     * @param orders
     * @param pageIndex
     * @param pageSize
     * @return
     */
    QueryPage<T> findByPage(Example<T> t, List<Order> orders, int pageIndex, int pageSize);

}

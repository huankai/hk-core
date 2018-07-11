package com.hk.core.service;

import com.hk.commons.util.AssertUtils;
import com.hk.core.data.commons.query.Order;
import com.hk.core.data.commons.query.QueryModel;
import com.hk.core.data.commons.query.QueryPage;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author: kevin
 * @date 2018-07-04 09:40
 */
public interface SelectService<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * @param id
     * @return
     */
    T findOne(ID id);

    /**
     * @param id
     * @return
     */
    T getOne(ID id);

    /**
     * @param t
     * @return
     */
    T findOne(T t);

    /**
     * @param t
     * @param orders
     * @param <S>
     * @return
     */
    List<T> findAll(T t, Order... orders);

    /**
     * @return
     */
    List<T> findAll();

    /**
     * @param ids
     * @return
     */
    Iterable<T> findByIds(Iterable<ID> ids);

    /**
     * @param ids
     * @return
     */
    default Iterable<T> findByIds(ID... ids) {
        AssertUtils.notNull(ids, "Ids must not be null");
        return findByIds(Arrays.asList(ids));
    }

    /**
     * 分页查询
     *
     * @param query 查询参数
     * @return 查询结果
     */
    QueryPage<T> queryForPage(QueryModel<T> query);

    /**
     * @param id
     * @return
     */
    boolean exists(ID id);

    /**
     * @param t
     * @return
     */
    boolean exists(T t);

    /**
     * @return
     */
    long count();

    /**
     * @return
     */
    long count(T t);

}

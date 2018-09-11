package com.hk.core.service;

import com.hk.commons.util.ArrayUtils;
import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.*;

/**
 * @author: kevin
 * @date 2018-07-04 09:40
 */
public interface SelectService<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * @param id id
     * @return Optional
     */
    Optional<T> findOne(ID id);

    /**
     * @param id id
     * @return T
     */
    T getOne(ID id);

    /**
     * @param t t
     * @return T
     */
    Optional<T> findOne(T t);


    /**
     * 查询所有
     *
     * @param orders 排序
     * @return
     */
    List<T> findAll(Order... orders);

    /**
     * 条件查询
     *
     * @param t      条件
     * @param orders 排序
     * @return List
     */
    List<T> findAll(T t, Order... orders);

    /**
     * @param ids
     * @return Collection
     */
    Collection<T> findByIds(Iterable<ID> ids);

    /**
     * @param ids ids
     * @return Collection
     */
    @SuppressWarnings("unchecked")
    default Collection<T> findByIds(ID... ids) {
        return ArrayUtils.isEmpty(ids) ? Collections.emptyList() : findByIds(Arrays.asList(ids));
    }

    /**
     * 分页查询
     *
     * @param query 查询参数
     * @return 查询结果
     */
    QueryPage<T> queryForPage(QueryModel<T> query);

    /**
     * @param id id
     * @return boolean
     */
    boolean exists(ID id);

    /**
     * @param t t
     * @return boolean
     */
    boolean exists(T t);

    /**
     * @return long
     */
    long count();

    /**
     * @return long
     */
    long count(T t);

}

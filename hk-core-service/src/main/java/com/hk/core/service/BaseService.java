package com.hk.core.service;

import com.hk.core.data.commons.query.Order;
import com.hk.core.data.commons.query.QueryModel;
import com.hk.core.data.commons.query.QueryPage;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;

/**
 * 基本CRUD操作
 *
 * @param <T>
 * @param <PK>
 * @author huangkai
 * @date 2017年9月27日下午5:04:48
 */
public interface BaseService<T extends Persistable<PK>, PK extends Serializable> {

    /**
     * 保存或更新
     *
     * @param entity
     * @return
     */
    boolean saveOrUpdate(T entity);

    /**
     * 批量保存或更新
     *
     * @param entities
     * @return
     */
    boolean saveOrUpdate(Iterable<T> entities);

    /**
     * @param id
     * @return
     */
    T findOne(PK id);

    /**
     * @param id
     * @return
     */
    T getOne(PK id);

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
    Iterable<T> findByIds(Iterable<PK> ids);

    /**
     * 分页查询
     *
     * @param query 查询参数
     * @return 查询结果
     */
    QueryPage<T> queryForPage(QueryModel query);

    /**
     * @param id
     * @return
     */
    boolean exists(PK id);

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

    /**
     * @param id
     */
    void deleteById(PK id);

    /**
     * @param entity
     */
    void delete(T entity);

    /**
     * @param entities
     */
    void delete(Iterable<? extends T> entities);

}

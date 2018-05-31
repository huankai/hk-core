package com.hk.core.service;

import com.hk.core.query.Order;
import com.hk.core.query.QueryModel;
import com.hk.core.query.QueryPageable;
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
    <S extends T> S saveOrUpdate(S entity);

    /**
     * 批量保存或更新
     *
     * @param entities
     * @return
     */
    <S extends T> List<S> saveOrUpdate(Iterable<S> entities);

    /**
     * @param entity
     * @return
     */
    <S extends T> S saveAndFlush(S entity);

    /**
     * 更新
     *
     * @param t
     * @return
     */
    default boolean saveFlushOrUpdate(T t) {
        return saveFlushOrUpdate(t, false);
    }

    /**
     * 更新，是否更新null属性
     *
     * @param t
     * @param updateNullField
     * @return
     */
    boolean saveFlushOrUpdate(T t, boolean updateNullField);

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
    <S extends T> S findOne(S t);

    /**
     * @param t
     * @param orders
     * @param <S>
     * @return
     */
    <S extends T> List<S> findAll(S t, Order... orders);

    /**
     * @return
     */
    List<T> findAll();

    /**
     * @param ids
     * @return
     */
    List<T> findAll(Iterable<PK> ids);

    /**
     * 分页查询
     *
     * @param query 查询参数
     * @return 查询结果
     */
    QueryPageable<T> queryForPage(QueryModel query);

    /**
     * Flushes all pending changes to the database.
     */
    void flush();

    /**
     * @param id
     * @return
     */
    boolean exists(PK id);

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
    void delete(PK id);

    /**
     * @param entity
     */
    void delete(T entity);

    /**
     * @param entities
     */
    void delete(Iterable<? extends T> entities);

}

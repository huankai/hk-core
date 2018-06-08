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
 * 基本CRUD操作
 *
 * @param <T>
 * @param <ID>
 * @author huangkai
 * @date 2017年9月27日下午5:04:48
 */
public interface BaseService<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * 保存或更新
     *
     * @param entity
     * @return
     */
    T saveOrUpdate(T entity);

    /**
     * 批量保存或更新
     *
     * @param entities
     * @return
     */
    Iterable<T> saveOrUpdate(Iterable<T> entities);

    /**
     * 更新不为空的字段
     *
     * @param entity
     * @return
     */
    T updateByIdSelective(T entity);

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

    /**
     * @param id
     */
    boolean deleteById(ID id);

    /**
     * @param ids
     */
    boolean deleteByIds(Iterable<ID> ids);

    /**
     * @param ids
     * @return
     */
    default boolean deleteByIds(ID... ids) {
        AssertUtils.notNull(ids, "Array Id must not be null");
        return deleteByIds(Arrays.asList(ids));
    }

    /**
     * @param entity
     */
    boolean delete(T entity);

    /**
     * @param entities
     */
    boolean delete(Iterable<T> entities);

}

package com.hk.core.data.commons.dao;

import com.hk.core.data.commons.query.Order;
import com.hk.core.data.commons.query.QueryModel;
import com.hk.core.data.commons.query.QueryPage;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author: kevin
 * @date 2018-07-12 15:02
 */
public interface BaseDao<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * 保存或更新
     *
     * @param t 保存或更新的实体
     * @return
     */
    T saveOrUpdate(T t);

    /**
     * 保存
     *
     * @param t 要保存的实体
     * @return
     */
    T save(T t);

    /**
     * 批量保存
     *
     * @param entities entities
     * @return
     */
    Collection<T> batchSave(Collection<T> entities);

    /**
     * 更新
     *
     * @param t 要更新的实体
     * @return
     */
    T update(T t);

    /**
     * 只更新不为空的实体
     *
     * @param t 要更新的不为空的属性
     * @return
     */
    T updateByIdSelective(T t);

    /**
     * 根据ID查询
     *
     * @param id 主键 id
     * @return
     */
    Optional<T> findById(ID id);

    /**
     * 主键删除
     *
     * @param id id
     */
    void deleteById(ID id);

    /**
     * 批量删除
     *
     * @param ids ids
     */
    void deleteByIds(Collection<ID> ids);

    /**
     * 批量删除
     *
     * @param entities
     */
    void delete(Collection<T> entities);

    /**
     * @param t
     */
    void delete(T t);


    /**
     * @param id
     * @return
     */
    Optional<T> findOne(ID id);

    /**
     * @param id
     * @return
     */
    T getOne(ID id);

    /**
     * @param t
     * @return
     */
    <S extends T> Optional<S> findOne(Example<S> t);

    /**
     * @param t
     * @param orders
     * @return
     */
    List<T> findAll(Example<T> t, Order... orders);

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
     * @param id
     * @return
     */
    boolean exists(ID id);

    /**
     * @param s
     * @return
     */
    <S extends T> boolean exists(Example<S> s);

    /**
     * @param example
     * @param <S>
     * @return
     */
    <S extends T> long count(Example<S> example);

    /**
     * @return
     */
    long count();

    /**
     * 查询分页
     *
     * @param example   example
     * @param orders    orders
     * @param pageIndex pageIndex
     * @param pageSize  pageSize
     * @return
     */
    QueryPage<T> findByPage(Example<T> example, List<Order> orders, int pageIndex, int pageSize);
}

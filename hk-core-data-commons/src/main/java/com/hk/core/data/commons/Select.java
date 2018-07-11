package com.hk.core.data.commons;

import com.hk.core.data.commons.query.Order;
import com.hk.core.data.commons.query.QueryPage;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;

/**
 * @author: kevin
 * @date 2018-6-6 22:50
 */
public interface Select<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    T getById(ID id);

    /**
     * 根据 ID查询
     *
     * @param id
     * @return
     */
    T findById(ID id);

    /**
     * 条件查询
     *
     * @param t
     * @param orders
     * @return
     */
    List<T> findAll(Example<T> t, Order... orders);

    /**
     * 多主键查询
     *
     * @param ids
     * @return
     */
    Iterable<T> findByIds(Iterable<ID> ids);

    /**
     * 无条件查询
     *
     * @return
     */
    List<T> findAll();

    /**
     * 查询单个
     *
     * @param t
     * @return
     */
    <S extends T> S findOne(Example<S> t);

    /**
     * 记录数
     *
     * @return
     */
    long count();

    /**
     * 条件记录数
     *
     * @param t
     * @return
     */
    <S extends T> long count(Example<S> t);

    /**
     * 主键记录是否存在
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
     * 分页查询
     *
     * @param t         查询条件
     * @param orders    查询排序
     * @param pageIndex 分页数(第几页)
     * @param pageSize  分页显示记录数
     * @return
     */
    QueryPage<T> findByPage(Example<T> t, List<Order> orders, int pageIndex, int pageSize);

}

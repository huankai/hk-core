package com.hk.core.data.jdbc.repository;

import com.hk.core.data.jdbc.SelectArguments;
import com.hk.core.data.jdbc.exception.EntityNotFoundException;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.page.ListResult;
import com.hk.core.query.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * @author: kevin
 * @date: 2018-10-10 10:09
 */
@NoRepositoryBean
public interface JdbcRepository<T, ID> extends PagingAndSortingRepository<T, ID> {

    /**
     * @param t      t
     * @param orders orders
     * @return List
     */
    ListResult<T> findAll(T t, Order... orders);


    Optional<T> findOne(CompositeCondition condition);

    /**
     * 查询唯一 ，必须返回一条记录
     *
     * @param condition condition
     * @return T t
     * @throws EntityNotFoundException EntityNotFound Exception
     */
    default T getOne(CompositeCondition condition) throws EntityNotFoundException {
        return findOne(condition).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * 查询唯一 ，必须返回一条记录
     *
     * @param t t
     * @return T
     * @throws EntityNotFoundException EntityNotFound Exception
     */
    default T getOne(T t) throws EntityNotFoundException {
        return findOne(t).orElseThrow(EntityNotFoundException::new);
    }


    Optional<T> findOne(T t);

    /**
     * 分页查询
     *
     * @param query query
     * @return QueryPage
     */
    QueryPage<T> queryForPage(QueryModel<T> query);

    /**
     * 分页查询
     *
     * @param arguments arguments
     * @return QueryPage
     */
    QueryPage<T> queryForPage(SelectArguments arguments);

    /**
     * 必须返回一条记录，不可能为空
     *
     * @param id id
     * @return T
     */
    T getById(ID id) throws EntityNotFoundException;

    /**
     * count 查询
     *
     * @param t t
     * @return count
     */
    long count(T t);

    /**
     * 只更新不为空 和 "" 的值
     *
     * @param t t
     * @return 更新后的实体
     */
    T updateByIdSelective(T t);

    /**
     * 查询
     *
     * @param condition condition
     * @param groupBys  groupBys
     * @param orders    orders
     * @return ListResult
     */
    ListResult<T> findAll(CompositeCondition condition, Collection<String> groupBys, Order... orders);

    /**
     * 根据条件删除，如果一个条件也没有，可能会删除所有
     *
     * @param conditions conditions
     */
    boolean delete(CompositeCondition conditions);
}

package com.hk.core.data.jdbc.repository;

import com.hk.core.data.jdbc.SelectArguments;
import com.hk.core.data.jdbc.exception.EntityNotFoundException;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.page.ListResult;
import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

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

    /**
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
     * @param t t
     * @return count
     */
    long count(T t);

    /**
     * 更新不能空的值
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
    ListResult<T> findAll(CompositeCondition condition, Set<String> groupBys, Order... orders);


    /**
     * 根据条件删除
     *
     * @param conditions conditions
     */
    boolean delete(CompositeCondition conditions);
}

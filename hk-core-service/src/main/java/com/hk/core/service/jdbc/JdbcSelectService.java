package com.hk.core.service.jdbc;

import com.hk.commons.util.ListResult;
import com.hk.core.data.jdbc.SelectArguments;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.query.QueryModel;
import com.hk.core.service.SelectService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

/**
 * @author kevin
 * @date 2018-10-11 14:46
 */
public interface JdbcSelectService<T extends Persistable<ID>, ID extends Serializable> extends SelectService<T, ID> {


    /**
     * 条件查询
     *
     * @param t      t
     * @param orders orders
     * @return {@link ListResult}
     */
    ListResult<T> findAll(T t, Order... orders);

    /**
     * 根据 id 查询，必须返回记录
     *
     * @param id id
     * @return T
     */
    T getById(ID id);


    /**
     * 查询唯一，如果查询有多条记录，只会返回第一条
     *
     * @param t t
     * @return T
     */
    Optional<T> findOne(T t);

    /**
     * 查询唯一，必须返回一条记录
     *
     * @param t t
     * @return T
     */
    T getOne(T t);

    /**
     * 查询唯一，只会返回第一条
     *
     * @param condition condition
     * @return t
     */
    Optional<T> findOne(CompositeCondition condition);

    /**
     * 查询唯一，必须返回一条记录
     *
     * @param condition condition
     * @return T
     */
    T getOne(CompositeCondition condition);

    /**
     * 分页查询
     *
     * @param query query
     * @return {@link QueryPage}
     */
    QueryPage<T> queryForPage(QueryModel<T> query);

    /**
     * 查询分页
     *
     * @param selectArguments selectArguments
     * @return {@link QueryPage}
     */
    QueryPage<T> queryForPage(SelectArguments selectArguments);

    /**
     * count 查询
     *
     * @param t t
     * @return count
     */
    long count(T t);

    /**
     * 条件查询
     *
     * @param condition condition
     * @param orders    orders
     * @return {@link ListResult}
     */
    default ListResult<T> findAll(CompositeCondition condition, Order... orders) {
        return findAll(condition, null, orders);
    }

    /**
     * 条件分组查询
     *
     * @param condition condition
     * @param groupBys  group by xxx
     * @param orders    排序
     * @return {@link ListResult}
     */
    ListResult<T> findAll(CompositeCondition condition, Collection<String> groupBys, Order... orders);
}

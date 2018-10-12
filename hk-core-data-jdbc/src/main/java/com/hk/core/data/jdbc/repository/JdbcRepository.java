package com.hk.core.data.jdbc.repository;

import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author: sjq-278
 * @date: 2018-10-10 10:09
 */
@NoRepositoryBean
public interface JdbcRepository<T, ID> extends PagingAndSortingRepository<T, ID> {

    /**
     * @param t      t
     * @param orders orders
     * @return List
     */
    List<T> findAll(T t, Order... orders);

    /**
     * @param query query
     * @return QueryPage
     */
    QueryPage<T> queryForPage(QueryModel<T> query);

    /**
     * @param t t
     * @return count
     */
    long count(T t);


}

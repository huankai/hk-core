package com.hk.core.service;

import com.hk.commons.util.ArrayUtils;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.query.QueryModel;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * @author kevin
 * @date 2018-07-04 09:40
 */
public interface SelectService<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * @param id id
     * @return {@link Optional}
     */
    Optional<T> findById(ID id);

    /**
     * 查询所有
     *
     * @param orders 排序
     * @return {@link Iterable}
     */
    Iterable<T> findAll(Order... orders);

    /**
     * @param ids
     * @return {@link Iterable}
     */
    Iterable<T> findByIds(Iterable<ID> ids);

    /**
     * @param ids ids
     * @return {@link Iterable}
     */
    @SuppressWarnings("unchecked")
    default Iterable<T> findByIds(ID... ids) {
        return ArrayUtils.isEmpty(ids) ? Collections.emptyList() : ((SelectService<T, ID>) Service.currentProxy(this)).findByIds(Arrays.asList(ids));
    }

    /**
     * 分页查询
     *
     * @param query 查询参数
     * @return {@link QueryPage}
     */
    QueryPage<T> queryForPage(QueryModel<T> query);

    /**
     * @param id id
     * @return boolean
     */
    boolean existsById(ID id);

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

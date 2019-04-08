package com.hk.core.service.jpa;

import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.service.SelectService;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author kevin
 * @date 2018-10-10 10:32
 */
public interface JpaSelectService<T extends Persistable<ID>, ID extends Serializable> extends SelectService<T, ID> {

    /**
     * 查询唯一
     *
     * @param id id
     * @return T
     */
    T getOne(ID id);

    /**
     * @param t t
     * @return T
     */
    Optional<T> findOne(T t);

    /**
     * 查询唯一
     *
     * @param spec spec
     * @return result
     */
    Optional<T> findOne(Specification<T> spec);

    /**
     * 分页查询
     *
     * @param spec      查询条件
     * @param pageIndex 分页参数
     * @param pageSize  分页参数
     * @param orders    排序
     * @return 查询结果集
     */
    QueryPage<T> findAll(Specification<T> spec, int pageIndex, int pageSize, Order... orders);

    /**
     * 集合查询
     *
     * @param spec   spec
     * @param orders 排序
     * @return 查询结果集
     */
    List<T> findAll(Specification<T> spec, Order... orders);

    /**
     * 查询记录数
     *
     * @param spec 查询条件
     * @return 记录数
     */
    long count(Specification<T> spec);
}

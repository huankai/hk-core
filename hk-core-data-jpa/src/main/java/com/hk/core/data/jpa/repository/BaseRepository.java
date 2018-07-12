package com.hk.core.data.jpa.repository;

import com.hk.core.data.commons.dao.BaseDao;
import com.hk.core.data.commons.query.Order;
import com.hk.core.data.commons.query.QueryModel;
import com.hk.core.data.commons.query.QueryPage;
import com.hk.core.data.commons.query.SimpleQueryPage;
import com.hk.core.data.commons.util.OrderUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;


/**
 * @param <T>
 * @param <ID>
 * @author: kevin
 */
@NoRepositoryBean
public interface BaseRepository<T extends Persistable<ID>, ID extends Serializable> extends BaseDao<T, ID>, JpaRepository<T, ID> {

    /**
     * 查询排序
     *
     * @param example example
     * @param orders  orders
     * @return
     */
    default List<T> findAll(Example<T> example, Order... orders) {
        return findAll(example, OrderUtils.toSort(orders));
    }


    /**
     * 分页查询
     *
     * @param example   example
     * @param orders    orders
     * @param pageIndex pageIndex
     * @param pageSize  pageSize
     * @return
     */
    @Override
    default QueryPage<T> findByPage(Example<T> example, List<Order> orders, int pageIndex, int pageSize) {
        Page<T> page = findAll(example, PageRequest.of(pageIndex, pageSize, OrderUtils.toSort(orders)));
        return new SimpleQueryPage<>(page.getContent(), page.getTotalElements(), pageIndex, pageSize);
    }


}

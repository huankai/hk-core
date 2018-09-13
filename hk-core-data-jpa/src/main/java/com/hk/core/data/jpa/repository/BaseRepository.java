package com.hk.core.data.jpa.repository;

import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.ObjectUtils;
import com.hk.core.data.jpa.util.OrderUtils;
import com.hk.core.page.QueryPage;
import com.hk.core.page.SimpleQueryPage;
import com.hk.core.query.Order;
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
public interface BaseRepository<T extends Persistable<ID>, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * 更新不为空的属性
     *
     * @param t t
     * @return T
     */
    default T updateByIdSelective(T t) {
        ID id = t.getId();
        if (ObjectUtils.isEmpty(id)) {
            throw new IllegalArgumentException("更新id不能为空！");
        }
        T find = getOne(id);
        BeanUtils.copyNotNullProperties(t, find);
        return save(find);
    }

    /**
     * 查询排序
     *
     * @param example example
     * @param orders  orders
     * @return List
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
     * @return QueryPage
     */
    default QueryPage<T> findByPage(Example<T> example, List<Order> orders, int pageIndex, int pageSize) {
        Page<T> page = findAll(example, PageRequest.of(pageIndex, pageSize, OrderUtils.toSort(orders)));
        return new SimpleQueryPage<>(page.getContent(), page.getTotalElements(), pageIndex, pageSize);
    }


}

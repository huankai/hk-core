package com.hk.core.service.jpa;

import com.hk.core.query.Order;
import com.hk.core.service.BaseService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;

/**
 * @author: sjq-278
 * @date: 2018-10-10 12:58
 */
public interface JpaBaseService<T extends Persistable<ID>, ID extends Serializable> extends BaseService<T, ID>, JpaSelectService<T, ID> {

    List<T> findAll(T t, Order... orders);

    T updateByIdSelective(T t);
}

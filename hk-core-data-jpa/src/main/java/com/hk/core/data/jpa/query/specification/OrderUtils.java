package com.hk.core.data.jpa.query.specification;

import org.hibernate.jpa.criteria.OrderImpl;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 * @author: huangkai
 * @date 2018-06-07 12:31
 */
class OrderUtils {

    public static Order toJpaOrder(Root<?> root, com.hk.core.data.commons.query.Order order) {
        Path expression = PathUtils.getPath(root, order.getField());
        return new OrderImpl(expression, !order.isDesc());
    }
}

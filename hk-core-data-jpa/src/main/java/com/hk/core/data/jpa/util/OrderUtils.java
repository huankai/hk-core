package com.hk.core.data.jpa.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.query.Order;

/**
 * @author: kevin
 * @date 2018-07-24 16:37
 */
public abstract class OrderUtils {


    /**
     * 转换为Spring Data Sort
     *
     * @param orders
     * @return
     */
    public static Sort toSort(List<Order> orders) {
        Sort sort = null;
        if (CollectionUtils.isNotEmpty(orders)) {
            List<Sort.Order> orderList = orders.stream()
                    .map(order -> new Sort.Order(order.isDesc() ? Sort.Direction.DESC : Sort.Direction.ASC, order.getField()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(orderList)) {
                sort = Sort.by(orderList);
            }
        }
        return sort;
    }


    /**
     * 转换为Spring Data Sort
     *
     * @param orders orders
     * @return
     */
    public static Sort toSort(Order... orders) {
        return ArrayUtils.isEmpty(orders) ? null : toSort(Arrays.asList(orders));
    }
}

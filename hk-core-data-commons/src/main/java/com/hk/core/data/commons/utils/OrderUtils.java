package com.hk.core.data.commons.utils;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.query.Order;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: kevin
 * @date: 2018-07-24 16:37
 */
public abstract class OrderUtils {

    /**
     * 转换为Spring Data Sort
     *
     * @param orders orders
     * @return Sort
     */
    public static Sort toSort(List<Order> orders) {
        Sort sort = Sort.unsorted();
        if (CollectionUtils.isNotEmpty(orders)) {
            List<Sort.Order> orderList = orders.stream()
                    .filter(order -> Objects.nonNull(SqlEscapeUtils.escape(order.getField())))
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
     * @return Sort
     */
    public static Sort toSort(Order... orders) {
        return toSort(ArrayUtils.asArrayList(orders));
    }

    public static List<Order> toOrderList(Sort sort) {
        List<Order> orders = new ArrayList<>();
        Iterator<Sort.Order> iterator = sort.iterator();
        iterator.forEachRemaining(item -> orders.add(new Order(item.getProperty(), item.isDescending())));
        return orders;
    }
}

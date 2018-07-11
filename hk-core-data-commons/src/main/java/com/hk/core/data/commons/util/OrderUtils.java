package com.hk.core.data.commons.util;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.data.commons.query.Order;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order util
 *
 * @author: kevin
 * @date 2018-07-04 12:12
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
                sort = new Sort(orderList);
            }
        }
        return sort;
    }


    /**
     * 转换为Spring Data Sort
     *
     * @param orders
     * @return
     */
    public static Sort toSort(Order... orders) {
        Sort sort = null;
        if (ArrayUtils.isNotEmpty(orders)) {
            sort = toSort(Arrays.asList(orders));
        }
        return sort;
    }
}

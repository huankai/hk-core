package com.hk.core.data.commons.utils;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.query.Order;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SQL排序工具类
 *
 * @author kevin
 * @date 2018-07-24 16:37
 */
public abstract class OrderUtils {

    /**
     * 转换为Spring Data Sort
     *
     * @param orders orders
     * @return {@link Sort}
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
     * @return {@link Sort}
     */
    public static Sort toSort(Order... orders) {
        return toSort(ArrayUtils.asArrayList(orders));
    }

    /**
     * Sort 转换为  Order
     *
     * @param sort {@link Sort}
     * @return 排序字段
     */
    public static List<Order> toOrderList(Sort sort) {
        List<Order> orders = new ArrayList<>();
        Iterator<Sort.Order> iterator = sort.iterator();
        iterator.forEachRemaining(item -> orders.add(new Order(item.getProperty(), item.isDescending())));
        return orders;
    }

    public static String toOrderSql(Collection<Order> orders) {
        StringBuilder orderSql = new StringBuilder();
        if (CollectionUtils.isNotEmpty(orders)) {
            orderSql.append(" ORDER BY ");
            int index = 0;
            for (Order order : orders) {
                if (index++ > 0) {
                    orderSql.append(StringUtils.COMMA_SEPARATE);
                }
                orderSql.append(order.toString());
            }
        }
        return orderSql.toString();
    }
}

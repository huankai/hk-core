package com.hk.core.query;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class QueryModel {

    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 查询分页参数
     */
    private int pageIndex;

    /**
     * 查询分页参数，默认为10
     */
    private int pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 分页时，不需要再count，所以用该属性来存上一次count的数字
     */
    private int totalRowCount;

    /**
     * 查询排序
     */
    private List<Order> orders = Lists.newArrayList();

    public final int getStartRowIndex() {
        int startRow = pageIndex;
        if (pageIndex < 1) {
            startRow = 1;
        }
        return (startRow - 1) * getPageSize();
    }

    /**
     * 不能小于等于0
     *
     * @return
     */
    public final int getPageSize() {
        return pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    /**
     * Jpa 查询分页从 0 开始
     *
     * @return
     */
    public final int getJpaStartRowIndex() {
        int startRow = pageIndex - 1;
        if (startRow < 0) {
            startRow = 0;
        }
        return startRow;
    }

    public List<org.springframework.data.domain.Sort.Order> getSortOrderList() {
        return orders.stream().map(Order::toSpringJpaOrder).collect(Collectors.toList());
    }

}

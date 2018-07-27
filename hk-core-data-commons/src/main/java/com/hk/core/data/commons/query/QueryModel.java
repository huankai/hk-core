package com.hk.core.data.commons.query;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * queryModel
 */
@Data
public class QueryModel<T> {

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
     * 查询参数
     */
    private T param;

    /**
     * 查询排序
     */
    private List<Order> orders = new ArrayList<>();

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

}
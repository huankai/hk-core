package com.hk.core.query;


import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.BeanUtils;
import lombok.Data;
import org.springframework.core.ResolvableType;

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
        return pageIndex <= 0 ? 0 : pageIndex - 1;
    }

    public T getParam() {
        if (null == param) {
            ResolvableType resolvableType = ResolvableType.forClass(QueryModel.class);
            @SuppressWarnings("unchecked")
            Class<T> resolve = (Class<T>) resolvableType.getGeneric(0).resolve();
            return resolve == null ? null : BeanUtils.instantiateClass(resolve);
        }
        return param;
    }

    /**
     * 不能小于等于0
     *
     * @return pageSize
     */
    public final int getPageSize() {
        return pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    /**
     * 添加排序到第一个
     *
     * @param order order
     * @return {@link QueryModel}
     */
    public QueryModel<T> addOrderToFirst(Order order) {
        this.orders.add(0, order);
        return this;
    }

    /**
     * 添加排序
     *
     * @param orders Orders
     * @return {@link QueryModel}
     */
    public QueryModel<T> addOrders(Order... orders) {
        this.orders.addAll(ArrayUtils.asArrayList(orders));
        return this;
    }
}

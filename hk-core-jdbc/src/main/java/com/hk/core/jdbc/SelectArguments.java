package com.hk.core.jdbc;

import com.hk.commons.util.CollectionUtils;
import com.hk.core.query.Order;
import com.hk.core.query.QueryModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author kevin
 * @date 2018-09-19 10:48
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SelectArguments extends DeleteArguments {

    /**
     * 去重
     */
    private boolean distinct = false;

    /**
     * 查询字段
     */
    private Collection<String> fields;

    /**
     * 分组
     */
    private Collection<String> groupBy = new LinkedHashSet<>();

    /**
     * Select count(*) || select count(id)
     * 默认所有主键字段为 id
     *
     * @see com.hk.core.data.jdbc.domain.AbstractUUIDPersistable#id
     */
    private String countField = "id";

    /**
     * 排序
     */
    private List<Order> orders = new ArrayList<>();

    /**
     * 分页参数
     */
    private int startRowIndex;

    /**
     * 分页参数
     */
    private int pageSize;

    public SelectArguments fields(String... fields) {
        if (null == this.fields) {
            this.fields = new LinkedHashSet<>();
        }
        CollectionUtils.addAllNotNull(this.fields, fields);
        return this;
    }

    public SelectArguments groupBys(String... groupBy) {
        CollectionUtils.addAllNotNull(this.groupBy, groupBy);
        return this;
    }

    public static SelectArguments newSelectArguments(QueryModel<?> query) {
        var arguments = new SelectArguments();
        arguments.setOrders(query.getOrders());
        arguments.setStartRowIndex(query.getStartRowIndex());
        arguments.setPageSize(query.getPageSize());
        return arguments;
    }
}

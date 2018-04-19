package com.hk.core.query;

import com.google.common.collect.Lists;
import com.hk.core.query.jdbc.CompositeCondition;
import com.hk.core.query.jdbc.SelectArguments;
import com.hk.core.query.jdbc.SimpleCondition;
import lombok.Data;

import java.util.List;

/**
 * @author huangkai
 * @date 2017年12月23日下午3:36:15
 */
@Data
public class JdbcQueryModel extends QueryModel {

    /**
     * 查询参数
     */
    private List<SimpleCondition> params;

    /**
     * And OR
     */
    private AndOr andOr = AndOr.AND;

    /**
     * 转换为SelectArguments对象
     *
     * @return SelectArguments
     */
    public SelectArguments toSelectArguments() {
        SelectArguments arguments = new SelectArguments();
        arguments.setOrders(getOrders());
        arguments.setPageIndex(getPageIndex());
        arguments.setPageSize(getPageSize());
        arguments.getConditions().addConditions(new CompositeCondition(andOr, Lists.newArrayList(params)));
        return arguments;
    }

}

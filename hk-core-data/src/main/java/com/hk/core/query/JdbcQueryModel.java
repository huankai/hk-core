package com.hk.core.query;

import com.google.common.collect.Lists;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.query.jdbc.CompositeCondition;
import com.hk.core.query.jdbc.Condition;
import com.hk.core.query.jdbc.SelectArguments;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author huangkai
 * @date 2017年12月23日下午3:36:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JdbcQueryModel extends QueryModel {

    /**
     * 查询参数
     */
    private List<QueryCondition> params;

    /**
     * And Or
     */
    private boolean or;

    /**
     * 不分页参数
     */
    private boolean paging;

    /**
     * 转换为SelectArguments对象
     *
     * @return SelectArguments
     */
    public SelectArguments toSelectArguments() {
        SelectArguments arguments = new SelectArguments();
        arguments.setOrders(getOrders());
        arguments.setPageIndex(getPageIndex());
        arguments.setPageSize(isPaging() ? 0 : getPageSize());
        if (CollectionUtils.isNotEmpty(params)) {
            List<Condition> conditions = Lists.newArrayList();
            params.forEach(item -> {
                Condition condition = item.toDataCondition();
                if (null != condition) {
                    conditions.add(condition);
                }
            });
            arguments.getConditions().addConditions(new CompositeCondition(or ? AndOr.OR : AndOr.AND, conditions));
        }
        return arguments;
    }

}

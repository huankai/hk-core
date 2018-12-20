package com.hk.core.data.jdbc.query;

import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.data.commons.query.AndOr;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin
 * @date 2018-09-19 11:03
 */
public class CompositeCondition implements Condition {

    @Setter
    @Getter
    private AndOr andOr;

    @Setter
    @Getter
    private List<Condition> conditions = new ArrayList<>();

    /**
     *
     */
    public CompositeCondition(Condition... conditions) {
        this(AndOr.AND, conditions);
    }

    /**
     * @param andOr      andOr
     * @param conditions conditions
     */
    public CompositeCondition(AndOr andOr, List<Condition> conditions) {
        this.andOr = andOr;
        this.conditions = conditions;
    }

    /**
     * @param andOr      andOr
     * @param conditions conditions
     */
    public CompositeCondition(AndOr andOr, Condition... conditions) {
        this.andOr = andOr;
        CollectionUtils.addAllNotNull(this.conditions, conditions);
    }

    /**
     * 添加条件
     *
     * @param condition condition
     */
    public CompositeCondition addCondition(Condition condition) {
        conditions.add(condition);
        return this;
    }

    /**
     * 添加条件
     *
     * @param conditions conditions
     */
    public CompositeCondition addConditions(Condition... conditions) {
        CollectionUtils.addAllNotNull(this.conditions, conditions);
        return this;
    }

    /**
     * 删除指定条件
     *
     * @param condition condition
     */
    public CompositeCondition removeCondition(Condition condition) {
        conditions.remove(condition);
        return this;
    }

    /**
     * 删除指定条件
     *
     * @param index index
     */
    public CompositeCondition removeCondition(int index) {
        conditions.remove(index);
        return this;
    }

    /**
     * 清除所有条件
     */
    public void clearConditions() {
        conditions.clear();
    }

    @Override
    public String toSqlString(List<Object> parameters) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Condition c : conditions) {
            if (null != c) {
                String sql = c.toSqlString(parameters);
                if (StringUtils.isNotEmpty(sql)) {
                    if (index++ > 0) {
                        sb.append(" ").append(andOr.toSqlString()).append(" ");
                    }
                    sb.append(sql);
                }
            }
        }
        return sb.length() > 0 ? sb.toString() : null;
    }
}

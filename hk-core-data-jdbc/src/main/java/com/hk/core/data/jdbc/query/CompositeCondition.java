package com.hk.core.data.jdbc.query;

import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.data.commons.query.AndOr;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sjq-278
 * @date: 2018-09-19 11:03
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
    public CompositeCondition() {
        this.andOr = AndOr.AND;
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
     * 添加条件
     *
     * @param condition condition
     */
    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    /**
     * 添加条件
     *
     * @param conditions conditions
     */
    public void addConditions(Condition... conditions) {
        CollectionUtils.addAllNotNull(this.conditions, conditions);
    }

    /**
     * 删除指定条件
     *
     * @param condition condition
     */
    public void removeCondition(Condition condition) {
        conditions.remove(condition);
    }

    /**
     * 删除指定条件
     *
     * @param index index
     */
    public void removeCondition(int index) {
        conditions.remove(index);
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
                        sb.append(" ");
                        sb.append(andOr.toSqlString());
                        sb.append(" ");
                    }
                    sb.append(sql);
                }
            }
        }
        return sb.length() > 0 ? sb.toString() : null;
    }
}

package com.hk.core.query.jdbc;

import com.google.common.collect.Lists;
import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.data.commons.query.AndOr;
import lombok.Data;

import java.util.List;

/**
 * @author huangkai
 * @date 2017年12月21日上午8:51:51
 */
@Data
public class CompositeCondition implements Condition {

    private AndOr andOr;

    private List<Condition> conditions = Lists.newArrayList();

    /**
     *
     */
    public CompositeCondition() {
        this.andOr = AndOr.AND;
    }

    /**
     * @param andOr
     * @param conditions
     */
    public CompositeCondition(AndOr andOr, List<Condition> conditions) {
        AssertUtils.notNull(conditions, "Conditions must not be null");
        this.andOr = andOr;
        this.conditions = conditions;
    }

    /**
     * add condition
     *
     * @param condition condition
     */
    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    /**
     * add condition
     *
     * @param conditions conditions
     */
    public void addConditions(Condition... conditions) {
        CollectionUtils.addAll(this.conditions, conditions);
    }

    /**
     * remove condition by condition
     *
     * @param condition condition
     */
    public void removeCondition(Condition condition) {
        conditions.remove(condition);
    }

    /**
     * remove condition by conditionIndex
     *
     * @param index index
     */
    public void removeCondition(int index) {
        conditions.remove(index);
    }

    /**
     * clear condition
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

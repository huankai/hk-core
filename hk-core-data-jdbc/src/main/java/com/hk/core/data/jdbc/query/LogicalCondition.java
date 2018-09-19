package com.hk.core.data.jdbc.query;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.data.commons.query.AndOr;

import java.util.List;

/**
 * @author: sjq-278
 * @date: 2018-09-19 11:07
 */
public class LogicalCondition extends CompositeCondition {

    /**
     * @param conditions conditions
     */
    public LogicalCondition(List<Condition> conditions) {
        this(AndOr.AND, conditions);
    }

    /**
     * @param conditions conditions
     */
    public LogicalCondition(Condition... conditions) {
        setAndOr(AndOr.AND);
        if (ArrayUtils.isNotEmpty(conditions)) {
            CollectionUtils.addAllNotNull(getConditions(), conditions);
        }
    }

    /**
     * @param andOr andOr
     * @param conditions conditions
     */
    public LogicalCondition(AndOr andOr, List<Condition> conditions) {
        setAndOr(andOr);
        setConditions(conditions);
    }

    @Override
    public String toSqlString(List<Object> parameters) {
        List<Condition> conditions = getConditions();
        if (CollectionUtils.isEmpty(conditions)) {
            return null;
        }
        AndOr andOr = getAndOr();
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Condition condition : conditions) {
            if (null != condition) {
                String sql = condition.toSqlString(parameters);
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
        if (sb.indexOf(andOr.toSqlString()) != -1) {
            sb.insert(0, "(").append(")");
        }
        return sb.toString();
    }
}

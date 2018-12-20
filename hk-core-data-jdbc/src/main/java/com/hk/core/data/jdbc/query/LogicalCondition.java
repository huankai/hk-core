package com.hk.core.data.jdbc.query;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.data.commons.query.AndOr;

import java.util.List;

/**
 * @author kevin
 * @date 2018-09-19 11:07
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
        this(AndOr.AND, ArrayUtils.asArrayList(conditions));
    }

    /**
     * @param andOr      andOr
     * @param conditions conditions
     */
    public LogicalCondition(AndOr andOr, List<Condition> conditions) {
        setAndOr(andOr);
        setConditions(conditions);
    }

    @Override
    public String toSqlString(List<Object> parameters) {
        String sql = super.toSqlString(parameters);
        AndOr andOr = getAndOr();
        if (StringUtils.containsIgnoreCase(sql, andOr.toSqlString())) {
            sql = String.format("(%s)", sql);
        }
        return sql;
    }
}

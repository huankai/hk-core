package com.hk.core.query;

import com.google.common.collect.Maps;
import com.hk.commons.util.StringUtils;
import com.hk.core.query.jdbc.Condition;
import com.hk.core.query.jdbc.DateRangeCondition;
import com.hk.core.query.jdbc.SimpleCondition;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author: huangkai
 * @date 2018-05-08 12:18
 */
@Data
public class QueryCondition {

    private static final String SIMPLE_CONDITION_KEY = "simple";

    private static final String DATERANGE_CONDITION_KEY = "dateRange";

    /**
     * condition is enable
     */
    private boolean enabled;

    /**
     * condition Field
     */
    private String name;

    /**
     * <pre>
     *     There are two values:
     *      <b>simple</b>: WHERE name operator value
     *      <b>dateRange</b>: WHERE (name operator start AND name operator end)
     * </pre>
     */
    private String type;

    /**
     * operator
     */
    private Operator operator;

    /**
     * <pre>
     *
     * The Query condition value
     * when The type value is simple,otherwise,this value is not valid.
     *
     * </pre>
     */
    private Object value;


    /* ***************************************dateRange Condition Start********************************************** */

    /**
     *
     */
    private DateRangeCondition.DateRange range;

    /**
     *
     */
    private LocalDateTime start;

    /**
     *
     */
    private LocalDateTime end;
    /* ***************************************dateRange Condition End********************************************** */

//    /**
//     *composite
//     */
//    private List<QueryCondition> conditions = Lists.newArrayList();

    public Condition toDataCondition() {
        if (!enabled) {
            return null;
        }
        String type = StringUtils.isEmpty(this.type) ? SIMPLE_CONDITION_KEY : this.type;
        ConditionBuilder builder = builderMap.get(type);
        if (null != builder) {
            return builder.buildCondition(this);
        }
        throw new RuntimeException("Cound not recognize the special condition type [" + type + "]");
    }

    @FunctionalInterface
    private interface ConditionBuilder {

        Condition buildCondition(QueryCondition condition);
    }

    private static final Map<String, ConditionBuilder> builderMap = Maps.newHashMap();

    static {
        builderMap.put(SIMPLE_CONDITION_KEY, condition -> {
            Operator oper = condition.operator;
            if (null == oper) {
                oper = Operator.EQ;
            }
            Object value = condition.value;
            if (value != null && oper != Operator.ISNULL
                    && oper != Operator.ISNOTNULL) {
                String stringValue = String.valueOf(value);
                if (StringUtils.isEmpty(stringValue))
                    return null;
                if (oper == Operator.IN
                        || oper == Operator.NOTIN) {
                    value = StringUtils.split(stringValue, "\\,");
                }
            }
            return new SimpleCondition(condition.name, condition.operator, value);
        });
        builderMap.put(DATERANGE_CONDITION_KEY, condition -> {
            DateRangeCondition dateRange = new DateRangeCondition();
            dateRange.setField(condition.name);
            dateRange.setRange(condition.range);
            dateRange.setStart(condition.start);
            dateRange.setEnd(condition.end);
            return dateRange;
        });
    }

}

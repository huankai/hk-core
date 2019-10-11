package com.hk.core.jdbc.query;

import com.hk.core.data.commons.query.Operator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author kevin
 * @date 2018-09-19 11:02
 */
@Data
@NoArgsConstructor
public class RangeCondition<T> implements Condition {

    private String field;

    private T start;

    private T end;

    private boolean includeStart;

    private boolean includeEnd;

    /**
     * @param field        field
     * @param start        start
     * @param end          end
     * @param includeStart includeStart
     * @param includeEnd   includeEnd
     */
    public RangeCondition(String field, T start, T end, boolean includeStart, boolean includeEnd) {
        this.field = field;
        this.start = start;
        this.end = end;
        this.includeStart = includeStart;
        this.includeEnd = includeEnd;
    }

    @Override
    public String toSqlString(List<Object> parameters) {
        CompositeCondition compos = new CompositeCondition();
        if (null != start) {
            compos.addCondition(new SimpleCondition(field, includeStart ? Operator.GTE : Operator.GT, start));
        }
        if (null != end) {
            compos.addCondition(new SimpleCondition(field, includeEnd ? Operator.LTE : Operator.LT, end));
        }
        return compos.toSqlString(parameters);
    }
}

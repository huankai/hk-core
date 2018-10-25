package com.hk.core.data.jdbc.query;

import com.hk.core.data.commons.query.Operator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: sjq-278
 * @date: 2018-09-19 11:02
 */
public class RangeCondition<T> implements Condition {

    @Getter
    @Setter
    private String field;

    @Getter
    @Setter
    private T start;

    @Getter
    @Setter
    private T end;

    @Getter
    @Setter
    private boolean includeStart;

    @Getter
    @Setter
    private boolean includeEnd;

    /**
     *
     */
    public RangeCondition() {
    }

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

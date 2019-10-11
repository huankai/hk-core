package com.hk.core.jdbc.query;

import com.hk.commons.util.ObjectUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.data.commons.query.MatchMode;
import com.hk.core.data.commons.query.Operator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author kevin
 * @date 2018-09-19 11:00
 */
@Data
@NoArgsConstructor
public class SimpleCondition implements Condition {

    @Getter
    private String field;

    @Getter
    private Operator operator;

    @Getter
    private Object value;

    private static Map<Operator, ComparableCondition> conditions;

    static {
        conditions = new HashMap<>();
        ComparableCondition c = new BinaryCondition();
        conditions.put(Operator.EQ, c);
        conditions.put(Operator.LT, c);
        conditions.put(Operator.LTE, c);
        conditions.put(Operator.GT, c);
        conditions.put(Operator.GTE, c);

        c = new InCondition();
        conditions.put(Operator.IN, c);
        conditions.put(Operator.NOTIN, c);

        conditions.put(Operator.NE, new NotEqCondition());
        conditions.put(Operator.ISNULL, new IsNullCondition());
        conditions.put(Operator.ISNOTNULL, new IsNotNullCondition());
        conditions.put(Operator.LIKE, new LikeExactCondition());
        conditions.put(Operator.LIKESTART, new LikeStartCondition());
        conditions.put(Operator.LIKEEND, new LikeEndCondition());
        conditions.put(Operator.LIKEANYWHERE, new LikeAnywhereCondition());

        conditions.put(Operator.BETWEEN, new BetweenCondition());
    }

    /**
     * @param field        field
     * @param compareValue compareValue
     */
    public SimpleCondition(String field, Object compareValue) {
        this(field, Operator.EQ, compareValue);
    }

    /**
     * @param field        field
     * @param operator     operator
     * @param compareValue compareValue
     */
    public SimpleCondition(String field, Operator operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String toSqlString(List<Object> parameters) {
        ComparableCondition c = getSubCondition(operator);
        return null != c ? c.toSqlString(field, operator, value, parameters) : null;
    }

    private ComparableCondition getSubCondition(Operator operator) {
        if (null == operator) {
            operator = Operator.EQ;
        }
        return conditions.get(operator);
    }

    private static abstract class ComparableCondition {

        abstract String toSqlString(String field, Operator operator, Object compareValue, List<Object> parameters);
    }

    private static class BetweenCondition extends ComparableCondition {

        @Override
        String toSqlString(String field, Operator operator, Object compareValue, List<Object> parameters) {
            if (Objects.isNull(compareValue) || !(compareValue instanceof Object[])) {
                return null;
            }
            Object[] value = (Object[]) compareValue;
            if (value.length != 2) {
                return null;
            }
            parameters.add(value[0]);
            parameters.add(value[1]);
            return String.format("(%s BETWEEN ? AND ?)", field);
        }

    }

    private static class InCondition extends ComparableCondition {

        @Override
        String toSqlString(String field, Operator operator, Object compareValue, List<Object> parameters) {
            Iterable<?> iterable = null;
            if (compareValue instanceof Object[]) {
                Object[] array = (Object[]) compareValue;
                iterable = Arrays.asList(array);
            } else if (compareValue instanceof Iterable<?>) {
                iterable = (Iterable<?>) compareValue;
            }
            if (iterable == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            int index = 0;
            for (Object value : iterable) {
                if (index++ > 0) {
                    sb.append(",");
                }
                sb.append("?");
                parameters.add(value);
            }
            if (index == 1) { //只有一个元素,使用 (xx = ?)
                return String.format("(%s %s ?)", field, operator == Operator.IN ? "=" : "<>");
            }
            String inWhich = sb.toString();
            if (inWhich.length() == 0) {
                // 空集合
                if (operator == Operator.IN) {
                    // xx in () 会报错；in一个空集合的结果应该是空，所以加一个1=2的条件
                    return "(1 = 2)";
                } else {
                    // not in 空集合，返回null，表示该条件不起作用。
                    return null;
                }
            }
            return String.format("(%s %s (%s))", field, operator == Operator.IN ? "IN" : "NOT IN", inWhich);
        }

    }

    private static abstract class BaseLikeCondition extends ComparableCondition {

        protected abstract MatchMode getLikeMatchMode();

        @Override
        protected String toSqlString(String field, Operator operator, Object compareValue, List<Object> parameters) {
            if (compareValue == null) {
                return null;
            }
            String value = String.valueOf(compareValue);
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            parameters.add(getLikeMatchMode().toMatchString(value));
            return String.format("(%s LIKE ?)", field);
        }
    }

    private static class LikeStartCondition extends BaseLikeCondition {

        @Override
        protected MatchMode getLikeMatchMode() {
            return MatchMode.START;
        }

    }

    private static class LikeEndCondition extends BaseLikeCondition {

        @Override
        protected MatchMode getLikeMatchMode() {
            return MatchMode.END;
        }

    }

    private static class LikeAnywhereCondition extends BaseLikeCondition {

        @Override
        protected MatchMode getLikeMatchMode() {
            return MatchMode.ANYWHERE;
        }

    }

    private static class LikeExactCondition extends BaseLikeCondition {

        @Override
        protected MatchMode getLikeMatchMode() {
            return MatchMode.EXACT;
        }

    }

    private static final class BinaryCondition extends ComparableCondition {

        @Override
        String toSqlString(String field, Operator operator, Object compareValue, List<Object> parameters) {
            if (ObjectUtils.isEmpty(compareValue)) {
                return null;
            }
            String sqlCompare = null;
            switch (operator) {
                case EQ:
                    sqlCompare = "=";
                    break;
                case LT:
                    sqlCompare = "<";
                    break;
                case LTE:
                    sqlCompare = "<=";
                    break;
                case GT:
                    sqlCompare = ">";
                    break;
                case GTE:
                    sqlCompare = ">=";
                    break;
                default:
                    break;
            }
            if (sqlCompare == null) {
                return null;
            }
            parameters.add(compareValue);
            return String.format("(%s %s ?)", field, sqlCompare);
        }

    }

    private static class NotEqCondition extends ComparableCondition {

        @Override
        String toSqlString(String field, Operator operator, Object compareValue, List<Object> parameters) {
            parameters.add(compareValue);
            return String.format("(%1$s <> ? OR (%1$s IS NULL))", field);
        }

    }

    private static class IsNullCondition extends ComparableCondition {

        @Override
        String toSqlString(String field, Operator operator, Object compareValue, List<Object> parameters) {
            return String.format("(%1$s IS NULL OR %1$s = '')", field);
        }
    }

    private static class IsNotNullCondition extends ComparableCondition {

        @Override
        String toSqlString(String field, Operator operator, Object compareValue, List<Object> parameters) {
            return String.format("(%s IS NOT NULL)", field);
        }
    }
}

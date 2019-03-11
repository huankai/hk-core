package com.hk.core.elasticsearch.query;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.ConverterUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.data.commons.query.Operator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.query.Criteria;

/**
 * @author huangkai
 * @date 2019/3/11 9:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCondition implements Condition {

    private String field;

    private Operator operator;

    private Object value;

    public SimpleCondition(String field, Object value) {
        this(field, Operator.EQ, value);
    }

    @Override
    public Criteria toElasticsearchCriteria() {
        if (StringUtils.isEmpty(field)) {
            return null;
        }
        if (operator == null) {
            operator = Operator.EQ;
        }
        switch (operator) {
            case EQ:
                return Criteria.where(field).is(value);
            case LIKE:
            case LIKEANYWHERE:
                return Criteria.where(field).contains(ConverterUtils.defaultConvert(value, String.class));
            case LIKESTART:
                return Criteria.where(field).endsWith(ConverterUtils.defaultConvert(value, String.class));
            case LIKEEND:
                return Criteria.where(field).startsWith(ConverterUtils.defaultConvert(value, String.class));
            case GT:
                return Criteria.where(field).greaterThan(value);
            case GTE:
                return Criteria.where(field).greaterThanEqual(value);
            case LT:
                return Criteria.where(field).lessThan(value);
            case LTE:
                return Criteria.where(field).lessThanEqual(value);
            case IN:
                if (value instanceof Iterable) {
                    return Criteria.where(field).in((Iterable<?>) value);
                } else {
                    return Criteria.where(field).in(value);
                }
            case NOTIN:
                if (value instanceof Iterable) {
                    return Criteria.where(field).not().in((Iterable<?>) value);
                } else {
                    return Criteria.where(field).not().in(value);
                }
            case NE:
                return Criteria.where(field).not().is(value);
            case BETWEEN:
                if (value instanceof Object[]) {
                    Object[] valueArr = (Object[]) value;
                    if (ArrayUtils.length(valueArr) == 2) {
                        return Criteria.where(field).between(valueArr[0], valueArr[1]);
                    }
                }
                return null;
        }
        return null;
    }
}

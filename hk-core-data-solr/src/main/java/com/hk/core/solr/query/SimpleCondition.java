package com.hk.core.solr.query;

import org.springframework.data.solr.core.query.Criteria;

import com.hk.commons.util.ConverterUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.data.commons.query.Operator;

import lombok.Data;

/**
 * @author sjq-278
 * @date 2018-12-03 16:49
 */
@Data
public class SimpleCondition implements Condition {

    private String field;

    private Operator operator;

    private Object value;

    public SimpleCondition() {
    }

    public SimpleCondition(String field, Object value) {
        this(field, Operator.EQ, value);
    }

    public SimpleCondition(String field, Operator operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Criteria toSolrCriteria() {
        if (StringUtils.isEmpty(field) || null == value) {
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
                if (value instanceof Iterable) {
                    return Criteria.where(field).contains((Iterable) value);
                }
                if (value instanceof String[]) {
                    return Criteria.where(field).contains((String[]) value);
                } else {
                    return Criteria.where(field).contains(ConverterUtils.defaultConvert(value, String.class));
                }
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
            case ISNULL:
                return Criteria.where(field).isNull();
            case ISNOTNULL:
                return Criteria.where(field).isNotNull();
            case NE:
                return Criteria.where(field).not().is(value);
            case BETWEEN:
                if (value instanceof Object[]) {
                    Object[] valueArr = (Object[]) value;
                    if(valueArr.length == 2) {
                    	return Criteria.where(field).between(valueArr[0], valueArr[1]);
                    }
                }
                return null;
        }
        return null;
    }
}

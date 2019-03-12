package com.hk.core.elasticsearch.query;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.ConverterUtils;
import com.hk.commons.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.query.Criteria;

import java.util.Objects;

/**
 * @author huangkai
 * @date 2019/3/11 9:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCondition implements Condition {

    private String field;

    private Criteria.OperationKey operator;

    private Object value;

    public SimpleCondition(String field, Object value) {
        this(field, Criteria.OperationKey.EQUALS, value);
    }

    @Override
    public Criteria toElasticsearchCriteria() {
        if (StringUtils.isEmpty(field)) {
            return null;
        }
        if (operator == null) {
            operator = Criteria.OperationKey.EQUALS;
        }
        switch (operator) {
            case EQUALS:
                return Criteria.where(field).is(value);
            case CONTAINS:
                String value = ConverterUtils.defaultConvert(this.value, String.class);
                return StringUtils.isEmpty(value) ? null : Criteria.where(field).contains(value);
            case EXPRESSION:
                return Criteria.where(field).expression(ConverterUtils.defaultConvert(this.value, String.class));
            case FUZZY:
                return Criteria.where(field).fuzzy(ConverterUtils.defaultConvert(this.value, String.class));
            case STARTS_WITH:
                value = ConverterUtils.defaultConvert(this.value, String.class);
                return StringUtils.isEmpty(value) ? null : Criteria.where(field).startsWith(value);
            case ENDS_WITH:
                value = ConverterUtils.defaultConvert(this.value, String.class);
                return StringUtils.isEmpty(value) ? null : Criteria.where(field).endsWith(value);
            case WITHIN:
                if (this.value instanceof Object[]) {
                    Object[] valueArr = (Object[]) this.value;
                    if (ArrayUtils.length(valueArr) == 2) {
                        String geoLocation = ConverterUtils.defaultConvert(valueArr[0], String.class);
                        String distance = ConverterUtils.defaultConvert(valueArr[0], String.class);
                        if (StringUtils.isNotEmpty(geoLocation) && StringUtils.isNotEmpty(distance)) {
                            return Criteria.where(field).within(geoLocation, distance);
                        }
                    }
                }
                return null;
            case BBOX:
                if (this.value instanceof Object[]) {
                    Object[] valueArr = (Object[]) this.value;
                    if (ArrayUtils.length(valueArr) == 2) {
                        String geoLocation = ConverterUtils.defaultConvert(valueArr[0], String.class);
                        String distance = ConverterUtils.defaultConvert(valueArr[1], String.class);
                        if (StringUtils.isNotEmpty(geoLocation) && StringUtils.isNotEmpty(distance)) {
                            return Criteria.where(field).boundedBy(geoLocation, distance);
                        }
                    }
                }
                return null;
            case GREATER:
                return Objects.nonNull(this.value) ? Criteria.where(field).greaterThan(this.value) : null;
            case GREATER_EQUAL:
                return Objects.nonNull(this.value) ? Criteria.where(field).greaterThanEqual(this.value) : null;
            case LESS:
                return Objects.nonNull(this.value) ? Criteria.where(field).lessThan(this.value) : null;
            case LESS_EQUAL:
                return Objects.nonNull(this.value) ? Criteria.where(field).lessThanEqual(this.value) : null;
            case IN:
                if (this.value instanceof Iterable) {
                    return Criteria.where(field).in((Iterable<?>) this.value);
                } else {
                    return Criteria.where(field).in(this.value);
                }
            case NOT_IN:
                if (this.value instanceof Iterable) {
                    return Criteria.where(field).not().in((Iterable<?>) this.value);
                } else {
                    return Criteria.where(field).not().in(this.value);
                }
            case BETWEEN:
                if (this.value instanceof Object[]) {
                    Object[] valueArr = (Object[]) this.value;
                    if (ArrayUtils.length(valueArr) == 2) {
                        String geoLocation = ConverterUtils.defaultConvert(valueArr[0], String.class);
                        String distance = ConverterUtils.defaultConvert(valueArr[1], String.class);
                        if (StringUtils.isNotEmpty(geoLocation) && StringUtils.isNotEmpty(distance)) {
                            return Criteria.where(field).between(valueArr[0], valueArr[1]);
                        }
                    }
                }
                return null;
            default:
                break;
        }
        return null;
    }
}

package com.hk.core.jdbc.query;

import com.hk.core.query.QueryModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kevin
 * @date 2019-8-30 11:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConditionQueryModel extends QueryModel<CompositeCondition> {

    public ConditionQueryModel() {
        setParam(new CompositeCondition());
    }

}

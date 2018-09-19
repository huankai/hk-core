package com.hk.core.data.jdbc;

import com.hk.core.data.jdbc.query.CompositeCondition;
import lombok.Data;

/**
 * @author: sjq-278
 * @date: 2018-09-19 14:01
 */
@Data
public class DeleteArguments {

    /**
     *
     */
    private String from;

    /**
     * where conditions
     */
    private CompositeCondition conditions = new CompositeCondition();
}

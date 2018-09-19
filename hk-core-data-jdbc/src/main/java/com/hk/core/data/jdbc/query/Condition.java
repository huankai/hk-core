package com.hk.core.data.jdbc.query;

import java.util.List;

/**
 * @author: sjq-278
 * @date: 2018-09-19 10:59
 */
public interface Condition {

    /**
     * 条件转换为 sql
     *
     * @param parameters parameters
     * @return sql
     */
    String toSqlString(List<Object> parameters);
}

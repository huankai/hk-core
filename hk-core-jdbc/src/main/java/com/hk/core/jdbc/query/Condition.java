package com.hk.core.jdbc.query;

import java.util.List;

/**
 * @author kevin
 * @date 2018-09-19 10:59
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

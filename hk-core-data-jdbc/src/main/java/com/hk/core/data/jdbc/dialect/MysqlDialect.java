package com.hk.core.data.jdbc.dialect;

/**
 * @author: kevin
 * @date: 2018-09-19 10:05
 */
public class MysqlDialect implements Dialect {

    @Override
    public String getLimitSql(String sql, int offset, int rows) {
        return String.format("%s LIMIT %d,%d", sql, offset, rows);
    }
}

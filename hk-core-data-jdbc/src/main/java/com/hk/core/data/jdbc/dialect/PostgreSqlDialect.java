package com.hk.core.data.jdbc.dialect;

/**
 * @author: sjq-278
 * @date: 2018-09-19 12:25
 */
public class PostgreSqlDialect implements Dialect {

    @Override
    public String getLimitSql(String sql, int offset, int rows) {
        return String.format("%s LIMIT %d OFFSET %d", sql, rows, offset);
    }
}

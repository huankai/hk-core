package com.hk.core.data.jdbc.dialect;

/**
 * @author kevin
 * @date 2018-09-19 10:05
 */
public class MysqlDialect implements Dialect {

    /**
     * mysql 的此种分页方式并不是最优的，对于大表分页查询可能性能不会很高
     *
     * @param sql    sql
     * @param offset offset
     * @param rows   rows
     * @return limit sql
     */
    @Override
    public String getLimitSql(String sql, int offset, int rows) {
        return String.format("%s LIMIT %d,%d", sql, offset, rows);
    }
}

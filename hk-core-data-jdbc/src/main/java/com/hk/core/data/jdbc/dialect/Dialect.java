package com.hk.core.data.jdbc.dialect;

/**
 * @author kevin
 * @date 2018-09-19 10:05
 */
public interface Dialect {

    String getLimitSql(String sql, int offset, int rows);

}

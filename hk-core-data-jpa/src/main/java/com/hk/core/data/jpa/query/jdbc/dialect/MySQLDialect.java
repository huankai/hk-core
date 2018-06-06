package com.hk.core.data.jpa.query.jdbc.dialect;

/**
 * MySql Dialect
 * 
 * @author kally
 * @date 2018年4月3日下午2:55:55
 */
public class MySQLDialect implements Dialect {

	@Override
	public String getLimitSql(String sql, int offset, int rows) {
		return String.format("%s LIMIT %d,%d", sql, offset, rows);
	}

}

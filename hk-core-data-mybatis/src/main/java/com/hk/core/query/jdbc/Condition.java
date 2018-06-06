/**
 * 
 */
package com.hk.core.query.jdbc;

import java.util.List;

/**
 * @author huangkai
 * @date 2017年12月20日下午2:43:21
 */
public interface Condition {

	String toSqlString(List<Object> parameters);

}

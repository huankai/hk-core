package com.hk.core.query.jdbc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.google.common.collect.Lists;
import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.ConverterUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.query.Order;
import com.hk.core.query.jdbc.dialect.Dialect;

/**
 * 
 * @author huangkai
 * @date 2017年12月11日下午1:30:11
 */
public class JdbcSession {

	private JdbcTemplate jdbcTemplate;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private Dialect dialect;

	/**
	 * 
	 */
	public JdbcSession() {
	}

	/**
	 * @param jdbcTemplate
	 * @param namedParameterJdbcTemplate
	 * @param dialect
	 */
	public JdbcSession(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
			Dialect dialect) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.dialect = dialect;
	}

	protected Dialect getDialect() {
		return dialect;
	}

	protected void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	protected JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	protected void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	protected void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	/**
	 * 
	 * @param arguments
	 * @param retriveRowCount
	 * @return
	 */
	public ListResult<Map<String, Object>> queryForList(SelectArguments arguments, boolean retriveRowCount) {
		return queryForList(arguments, retriveRowCount, new ColumnMapRowMapper());
	}

	/**
	 * 查询唯一
	 * 
	 * @param arguments
	 * @param clazz
	 * @return
	 */
	public final <T> T queryForObject(SelectArguments arguments, Class<T> clazz) {
		SelectStatement statement = buildSelect(arguments);
		BeanPropertyRowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(clazz);
		rowMapper.setConversionService(ConverterUtils.DEFAULT_CONVERSIONSERVICE);
		return jdbcTemplate.queryForObject(statement.selectSql.toString(), rowMapper, statement.parameters.toArray());
	}

	/**
	 * 查询返回对象，支持驼峰命名的属性
	 * 
	 * @param arguments
	 * @param retriveRowCount
	 * @param clazz
	 * @return
	 */
	public <T> ListResult<T> queryForList(SelectArguments arguments, boolean retriveRowCount, Class<T> clazz) {
		BeanPropertyRowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(clazz);
		rowMapper.setConversionService(ConverterUtils.DEFAULT_CONVERSIONSERVICE);
		return queryForList(arguments, retriveRowCount, rowMapper);
	}

	/**
	 * 
	 * @param arguments
	 * @param retriveRowCount
	 * @param rowMapper
	 * @return
	 */
	private <T> ListResult<T> queryForList(SelectArguments arguments, boolean retriveRowCount, RowMapper<T> rowMapper) {
		SelectStatement stmt = buildSelect(arguments);
		Object[] params = stmt.parameters.toArray();
		List<T> queryResult = queryForList(stmt.selectSql.toString(), rowMapper, arguments.getStartRowIndex(),
				arguments.getPageSize(), params);
		long rowCount = queryResult.size();
		if (retriveRowCount) {
			rowCount = queryForScalar(stmt.countSql.toString(), Long.class, params);
		}
		return new ListResult<>(rowCount, queryResult);
	}

	/**
	 * 
	 * @param sql
	 * @param clazz
	 * @param args
	 * @return
	 */
	private <T> T queryForScalar(String sql, Class<T> clazz, Object... args) {
		return jdbcTemplate.queryForObject(sql, clazz, args);
	}

	private <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, int offset, int rows, Object... args) {
		if (offset >= 0 && rows > 0) {
			sql = dialect.getLimitSql(sql, offset, rows);
		}
		return queryForList(sql, rowMapper, args);
	}

	private <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... args) {
		return jdbcTemplate.query(sql, rowMapper, args);
	}

	private SelectStatement buildSelect(SelectArguments arguments) {
		AssertUtils.notNull(arguments, "arguments must not be nulll");
		AssertUtils.notBlank(arguments.getFrom());
		StringBuilder sql = new StringBuilder();
		StringBuilder countSql = new StringBuilder();
		String fields = CollectionUtils.isEmpty(arguments.getFields()) ? " * "
				: StringUtils.join(arguments.getFields(), ",");
		sql.append("SELECT ");
		countSql.append("SELECT ");
		if (arguments.isDistinct()) {
			sql.append("DISTINCT ");
			countSql.append("DISTINCT ");
		}
		sql.append(fields);
		sql.append(" FROM ");
		countSql.append("count(*) FROM ");

		sql.append(arguments.getFrom());
		countSql.append(arguments.getFrom());

		List<Object> parameters = Lists.newArrayList();
		CompositeCondition condition = arguments.getConditions();
		if (null != condition) {
			String conditionSql = condition.toSqlString(parameters);
			if (StringUtils.isNotEmpty(conditionSql)) {
				sql.append(" WHERE ");
				sql.append(conditionSql);
				countSql.append(" WHERE ");
				countSql.append(conditionSql);
			}
		}
		Set<String> groupBys = arguments.getGroupBys();
		if (CollectionUtils.isNotEmpty(groupBys)) {
			String groupBySql = groupBys.stream().collect(Collectors.joining(StringUtils.COMMA_SEPARATE));
			sql.append(" GROUP BY ");
			sql.append(groupBySql);
			countSql.append(" GROUP BY ");
			countSql.append(groupBySql);
		}

		List<Order> orders = arguments.getOrders();
		if (CollectionUtils.isNotEmpty(orders)) {
			sql.append(" ORDER BY ");
			int index = 0;
			for (Order order : orders) {
				if (index++ > 0) {
					sql.append(StringUtils.COMMA_SEPARATE);
				}
				sql.append(order.toString());
			}
		}
		return new SelectStatement(sql, countSql, parameters);
	}

	private class SelectStatement {

		public StringBuilder selectSql;

		public StringBuilder countSql;

		public List<Object> parameters;

		private SelectStatement(StringBuilder selectSql, StringBuilder countSql, List<Object> parameters) {
			this.selectSql = selectSql;
			this.countSql = countSql;
			this.parameters = parameters;
		}
	}

}

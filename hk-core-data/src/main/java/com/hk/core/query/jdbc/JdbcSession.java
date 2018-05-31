package com.hk.core.query.jdbc;

import com.google.common.collect.Lists;
import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.ConverterUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.query.Order;
import com.hk.core.query.jdbc.dialect.Dialect;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author huangkai
 * @date 2017年12月11日下午1:30:11
 */
public class JdbcSession {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final Dialect dialect;

    /**
     * @param jdbcTemplate               jdbcTemplate
     * @param namedParameterJdbcTemplate namedParameterJdbcTemplate
     * @param dialect                    dialect
     */
    public JdbcSession(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                       Dialect dialect) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.dialect = dialect;
    }

    /**
     * 更新
     *
     * @param sql
     * @param paramMap
     * @return
     */
    public boolean update(Update update) {
        String sql = update.toSqlString();
        return null != sql && namedParameterJdbcTemplate.update(update.toSqlString(), update.getParamMap()) > 0;
    }

    /**
     * 更新
     *
     * @param sql
     * @param args
     * @return
     */
    public boolean udpate(String sql, Object... args) {
        return jdbcTemplate.update(sql, args) > 0;
    }

    /**
     * @param arguments       arguments
     * @param retriveRowCount retriveRowCount
     * @return
     */
    public ListResult<Map<String, Object>> queryForList(SelectArguments arguments, boolean retriveRowCount) {
        return queryForList(arguments, retriveRowCount, new ColumnMapRowMapper());
    }

    /**
     * 查询唯一
     *
     * @param arguments arguments
     * @param clazz     clazz
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
     * @param arguments       arguments
     * @param retriveRowCount retriveRowCount
     * @param clazz           clazz
     * @return
     */
    public <T> ListResult<T> queryForList(SelectArguments arguments, boolean retriveRowCount, Class<T> clazz) {
        BeanPropertyRowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(clazz);
        rowMapper.setConversionService(ConverterUtils.DEFAULT_CONVERSIONSERVICE);
        return queryForList(arguments, retriveRowCount, rowMapper);
    }

    /**
     * @param arguments       arguments
     * @param retriveRowCount retriveRowCount
     * @param rowMapper       rowMapper
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
     * @param sql   sql
     * @param clazz clazz
     * @param args  args
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
        AssertUtils.notBlank(arguments.getFrom(), "argument from must not be null");
        StringBuilder sql = new StringBuilder();
        StringBuilder countSql = new StringBuilder();
        String fields = CollectionUtils.isEmpty(arguments.getFieldSet()) ? " * "
                : arguments.getFieldSet().stream().collect(Collectors.joining(StringUtils.COMMA_SEPARATE));
        sql.append("SELECT ");
        countSql.append("SELECT ");
        if (arguments.isDistinct()) {
            sql.append("DISTINCT ");
            countSql.append("DISTINCT ");
        }
        sql.append(fields);
        sql.append(" FROM ");
        countSql.append("count(*) FROM ");

        sql.append(arguments.getFrom());//a join b on a.id = b
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

        private StringBuilder selectSql;

        private StringBuilder countSql;

        private List<Object> parameters;

        private SelectStatement(StringBuilder selectSql, StringBuilder countSql, List<Object> parameters) {
            this.selectSql = selectSql;
            this.countSql = countSql;
            this.parameters = parameters;
        }
    }

}

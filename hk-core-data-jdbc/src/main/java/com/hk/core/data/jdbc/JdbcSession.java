package com.hk.core.data.jdbc;

import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.ConverterUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.data.jdbc.core.HumpColumnMapRowMapper;
import com.hk.core.data.jdbc.dialect.Dialect;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.page.ListResult;
import com.hk.core.page.QueryPage;
import com.hk.core.page.SimpleQueryPage;
import com.hk.core.query.Order;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: kevin
 * @date: 2018-09-19 10:16
 */
public final class JdbcSession {

    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Dialect dialect;

    public JdbcSession(NamedParameterJdbcTemplate namedParameterJdbcTemplate, Dialect dialect) {
        this.jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.dialect = dialect;
    }

    /**
     * Delete
     *
     * @param arguments arguments
     */
    public boolean delete(DeleteArguments arguments) {
        AssertUtils.hasText(arguments.getFrom(), "delete From must not be null");
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(arguments.getFrom());
        List<Object> params = new ArrayList<>();
        String conditionSql = arguments.getConditions().toSqlString(params);
        if (StringUtils.isNotEmpty(conditionSql)) {
            sql.append(" WHERE ").append(conditionSql);
        }
        return jdbcTemplate.update(sql.toString(), params.toArray()) > 0;
    }

    /**
     * @param sql       sql
     * @param arguments arguments
     */
    public boolean update(String sql, Map<String, ?> arguments) {
        return namedParameterJdbcTemplate.update(sql, arguments) > 0;
    }


    /**
     * todo 未完成
     */
//    public void batchInsert(PersistentEntityInfo persistentEntityInfo, SqlParameterSource[] parameterSources) {
////        SqlParameterSourceUtils.createBatch()
//        namedParameterJdbcTemplate.batchUpdate(String.format("INSERT INTO %s(%s) VALUES (%s)",
//                persistentEntityInfo.getTableName(), "", ""), parameterSources);
//    }

    /**
     * @param arguments       arguments
     * @param retriveRowCount retriveRowCount
     * @return ListResult
     */
    public ListResult<Map<String, Object>> queryForList(SelectArguments arguments, boolean retriveRowCount) {
        return queryForList(arguments, retriveRowCount, new HumpColumnMapRowMapper());
    }

    /**
     * 查询唯一
     *
     * @param arguments arguments
     * @param clazz     clazz
     * @return T
     */
    public final <T> T queryForObject(SelectArguments arguments, Class<T> clazz) {
        SelectStatement statement = buildSelect(arguments);
        BeanPropertyRowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(clazz);
        rowMapper.setConversionService(ConverterUtils.DEFAULT_CONVERSION_SERVICE);
        return jdbcTemplate.queryForObject(statement.selectSql.toString(), rowMapper, statement.parameters.toArray());
    }

    /**
     * count 查询
     *
     * @param arguments arguments
     * @return 记录数
     */
    public long queryForCount(SelectArguments arguments) {
        SelectStatement statement = buildSelect(arguments);
        return queryForScalar(statement.countSql.toString(), Long.class, statement.parameters.toArray());
    }

    /**
     * 查询返回对象，支持驼峰命名的属性
     *
     * @param arguments       arguments
     * @param retriveRowCount retriveRowCount
     * @param clazz           clazz
     * @return ListResult
     */
    public <T> ListResult<T> queryForList(SelectArguments arguments, boolean retriveRowCount, Class<T> clazz) {
        BeanPropertyRowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(clazz);
        rowMapper.setConversionService(ConverterUtils.DEFAULT_CONVERSION_SERVICE);
        return queryForList(arguments, retriveRowCount, rowMapper);
    }

    /**
     * 分页查询
     *
     * @param arguments arguments
     * @param clazz     clazz
     * @param <T>       <T>
     * @return QueryPage<T>
     */
    public <T> QueryPage<T> queryForPage(SelectArguments arguments, Class<T> clazz) {
        ListResult<T> result = queryForList(arguments, true, clazz);
        return new SimpleQueryPage<>(result.getResult(), result.getTotalRowCount(), arguments.getStartRowIndex(), arguments.getPageSize());
    }

    /**
     * @param arguments       arguments
     * @param retriveRowCount retriveRowCount
     * @param rowMapper       rowMapper
     * @return ListResult
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
     * @return T
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
        AssertUtils.notNull(arguments, "arguments must not be null");
        AssertUtils.notBlank(arguments.getFrom(), "查询表名不能为空");
        StringBuilder sql = new StringBuilder();
        StringBuilder countSql = new StringBuilder();

        Set<String> fieldSet = arguments.getFields();
        String fields = CollectionUtils.isEmpty(fieldSet) ? "*"
                : fieldSet.stream().collect(Collectors.joining(","));
        sql.append("SELECT ");
        countSql.append("SELECT ");
        if (arguments.isDistinct()) {
            sql.append("DISTINCT ");
            countSql.append("DISTINCT ");
        }
        sql.append(fields);
        sql.append(" FROM ");
        String countField = arguments.getCountField();
        countSql.append("COUNT(").append(StringUtils.isEmpty(countField) ? "*" : countField).append(") FROM ");

        sql.append(arguments.getFrom());
        countSql.append(arguments.getFrom());

        List<Object> parameters = new ArrayList<>();
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
        Set<String> groupBys = arguments.getGroupBy();
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

        StringBuilder selectSql;

        StringBuilder countSql;

        List<Object> parameters;

        private SelectStatement(StringBuilder selectSql, StringBuilder countSql, List<Object> parameters) {
            this.selectSql = selectSql;
            this.countSql = countSql;
            this.parameters = parameters;
        }
    }


}

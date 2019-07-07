package com.hk.core.data.jdbc;

import com.hk.commons.util.*;
import com.hk.core.data.jdbc.core.CustomBeanPropertyRowMapper;
import com.hk.core.data.jdbc.core.HumpColumnMapRowMapper;
import com.hk.core.data.jdbc.core.namedparam.SqlParameterSourceUtils;
import com.hk.core.data.jdbc.dialect.Dialect;
import com.hk.core.data.jdbc.exception.NonUniqueResultException;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.page.QueryPage;
import com.hk.core.page.SimpleQueryPage;
import com.hk.core.query.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

/**
 * @author kevin
 * @date 2018-09-19 10:16
 */
@Slf4j
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
        AssertUtils.isTrue(StringUtils.isNotEmpty(arguments.getFrom()), "delete From must not be null");
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(arguments.getFrom());
        List<Object> params = new ArrayList<>();
        String conditionSql = arguments.getConditions().toSqlString(params);
        if (StringUtils.isNotEmpty(conditionSql)) {
            sql.append(" WHERE ").append(conditionSql);
        }
        return jdbcTemplate.update(sql.toString(), params.toArray()) > 0;
    }

    /**
     * 更新
     *
     * @param sql       sql
     * @param arguments arguments
     */
    public boolean update(String sql, Map<String, ?> arguments) {
        return namedParameterJdbcTemplate.update(sql, arguments) > 0;
    }

    /**
     * 批量更新
     *
     * @param sql  sql
     * @param args 参数
     * @return true or false
     */
    public boolean batchUpdate(String sql, List<Object[]> args) {
        return jdbcTemplate.batchUpdate(sql, args).length > 0;
    }

    /**
     * 批量更新
     *
     * @param sql  sql
     * @param args args
     * @param <T>  T
     * @return true
     */
    public <T> boolean batchUpdate(String sql, Collection<T> args) {
        namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(args));
        return true;
    }

    /**
     * 集合查询
     *
     * @param arguments       arguments
     * @param retriveRowCount retriveRowCount
     * @return {@link ListResult}
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
        CustomBeanPropertyRowMapper<T> rowMapper = CustomBeanPropertyRowMapper.newInstance(clazz);
//        rowMapper.setConversionService(ConverterUtils.DEFAULT_CONVERSION_SERVICE);
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
     * @return {@link ListResult}
     */
    public <T> ListResult<T> queryForList(SelectArguments arguments, boolean retriveRowCount, Class<T> clazz) {
        CustomBeanPropertyRowMapper<T> rowMapper = CustomBeanPropertyRowMapper.newInstance(clazz);
//        rowMapper.setConversionService(ConverterUtils.DEFAULT_CONVERSION_SERVICE);
        return queryForList(arguments, retriveRowCount, rowMapper);
    }

    /**
     * 分页查询
     *
     * @param arguments arguments
     * @param clazz     clazz
     * @param <T>       <T>
     * @return {@link QueryPage}
     */
    public <T> QueryPage<T> queryForPage(SelectArguments arguments, Class<T> clazz) {
        ListResult<T> result = queryForList(arguments, true, clazz);
        return new SimpleQueryPage<>(result.getResult(), result.getTotalRowCount(), arguments.getStartRowIndex(), arguments.getPageSize());
    }

    /**
     * 集合查询
     *
     * @param arguments       arguments
     * @param retriveRowCount retriveRowCount
     * @param rowMapper       rowMapper
     * @return {@link ListResult}
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
     * 查询
     *
     * @param sql   sql
     * @param clazz clazz
     * @param args  args
     * @return T
     */
    private <T> T queryForScalar(String sql, Class<T> clazz, Object... args) {
        return jdbcTemplate.queryForObject(sql, clazz, args);
    }

    /**
     * 只查询一条记录
     * <p>
     * select fields from table_name where condition1 = ? and condition2 = ? limit 0 ,2
     * </p>
     *
     * @param arguments arguments
     * @param clazz     clazz
     * @param <T>       T
     * @return T
     * @throws NonUniqueResultException 查询返回多条记录时抛出异常
     */
    public <T> Optional<T> queryForOne(SelectArguments arguments, Class<T> clazz) {
        CustomBeanPropertyRowMapper<T> rowMapper = CustomBeanPropertyRowMapper.newInstance(clazz);
        rowMapper.setConversionService(ConverterUtils.DEFAULT_CONVERSION_SERVICE);
        SelectStatement stmt = buildSelect(arguments);
        final String originalSql = stmt.selectSql.toString();
        String sql = dialect.getLimitSql(originalSql, 0, 2); // 分页两条,如果返回有多条记录,抛出异常
        List<T> result = queryForList(sql, rowMapper, stmt.parameters.toArray());
        if (result.size() > 1) {
            if (log.isErrorEnabled()) {
                log.error("查询结果不唯一,返回多条记录: SQL : {},args:{}", originalSql, stmt.parameters);
                List<Object> parameters = stmt.parameters;
                if (CollectionUtils.isNotEmpty(parameters)) {
                    log.error("args:{}", parameters);
                }
            }
            throw new NonUniqueResultException("查询结果不唯一,SQL:" + originalSql);
        }
        return CollectionUtils.getFirstOrDefault(result);
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
        AssertUtils.isTrue(Objects.nonNull(arguments), "arguments must not be null");
        AssertUtils.notEmpty(arguments.getFrom(), "查询表名不能为空");
        StringBuilder sql = new StringBuilder();
        StringBuilder countSql = new StringBuilder();

        Collection<String> fieldSet = arguments.getFields();
        String fields = CollectionUtils.isEmpty(fieldSet) ? "*"
                : String.join(StringUtils.COMMA_SEPARATE, fieldSet);
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
        Collection<String> groupBys = arguments.getGroupBy();
        if (CollectionUtils.isNotEmpty(groupBys)) {
            String groupBySql = String.join(StringUtils.COMMA_SEPARATE, groupBys);
            sql.append(" GROUP BY ").append(groupBySql);
            countSql.append(" GROUP BY ").append(groupBySql).append(") result_");
            countSql.insert(0, "SELECT COUNT(*) FROM (");
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

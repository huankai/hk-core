package com.hk.core.jdbc;

import com.hk.commons.util.*;
import com.hk.core.data.commons.utils.OrderUtils;
import com.hk.core.jdbc.core.CustomBeanPropertyRowMapper;
import com.hk.core.jdbc.core.HumpColumnMapRowMapper;
import com.hk.core.jdbc.core.namedparam.SqlParameterSourceUtils;
import com.hk.core.jdbc.dialect.Dialect;
import com.hk.core.jdbc.exception.NonUniqueResultException;
import com.hk.core.page.QueryPage;
import com.hk.core.page.SimpleQueryPage;
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
        var sql = new StringBuilder("DELETE FROM ").append(arguments.getFrom());
        var params = new ArrayList<>();
        var conditionSql = arguments.getConditions().toSqlString(params);
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
     * @param arguments        arguments
     * @param retrieveRowCount retrieveRowCount
     * @return {@link ListResult}
     */
    public ListResult<Map<String, Object>> queryForList(SelectArguments arguments, boolean retrieveRowCount) {
        return queryForList(arguments, retrieveRowCount, new HumpColumnMapRowMapper());
    }

    /**
     * 查询唯一
     *
     * @param arguments arguments
     * @param clazz     clazz
     * @return T
     */
    public final <T> T queryForObject(SelectArguments arguments, Class<T> clazz) {
        var statement = buildSelect(arguments);
        var rowMapper = CustomBeanPropertyRowMapper.newInstance(clazz);
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
        var statement = buildSelect(arguments);
        return queryForScalar(statement.countSql.toString(), Long.class, statement.parameters.toArray());
    }

    /**
     * 查询返回对象，支持驼峰命名的属性
     *
     * @param arguments        arguments
     * @param retrieveRowCount retrieveRowCount
     * @param clazz            clazz
     * @return {@link ListResult}
     */
    public <T> ListResult<T> queryForList(SelectArguments arguments, boolean retrieveRowCount, Class<T> clazz) {
        var rowMapper = CustomBeanPropertyRowMapper.newInstance(clazz);
//        rowMapper.setConversionService(ConverterUtils.DEFAULT_CONVERSION_SERVICE);
        return queryForList(arguments, retrieveRowCount, rowMapper);
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
        var result = queryForList(arguments, true, clazz);
        return new SimpleQueryPage<>(result.getResult(), result.getTotalRowCount(), arguments.getStartRowIndex() + 1, arguments.getPageSize());
    }

    /**
     * 集合查询
     *
     * @param arguments        arguments
     * @param retrieveRowCount retrieveRowCount
     * @param rowMapper        rowMapper
     * @return {@link ListResult}
     */
    private <T> ListResult<T> queryForList(SelectArguments arguments, boolean retrieveRowCount, RowMapper<T> rowMapper) {
        var stmt = buildSelect(arguments);
        var params = stmt.parameters.toArray();
        var rowCount = 0L;
        if (retrieveRowCount) {// 如果是分页查询，先查询记录数，如果记录数为 0 ，直接返回，不再查询数据集
            rowCount = queryForScalar(stmt.countSql.toString(), Long.class, params);
            if (rowCount == 0) {
                return new ListResult<>(rowCount, new ArrayList<>());
            }
        }
        var queryResult = queryForList(stmt.selectSql.toString(), rowMapper,
                arguments.getStartRowIndex() * arguments.getPageSize(),
                arguments.getPageSize(), params);
        if (0 == rowCount) {
            rowCount = queryResult.size();
        }
        return new ListResult<>(rowCount, queryResult);
    }

    public <T> T queryForScalar(SelectArguments arguments, Class<T> clazz) {
        var statement = buildSelect(arguments);
        return queryForScalar(statement.selectSql.toString(), clazz, statement.parameters.toArray());
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
        var rowMapper = CustomBeanPropertyRowMapper.newInstance(clazz);
        rowMapper.setConversionService(ConverterUtils.DEFAULT_CONVERSION_SERVICE);
        var stmt = buildSelect(arguments);
        final var originalSql = stmt.selectSql.toString();
        var sql = dialect.getLimitSql(originalSql, 0, 2); // 分页两条,如果返回有多条记录,抛出异常
        var result = queryForList(sql, rowMapper, stmt.parameters.toArray());
        if (result.size() > 1) {
            log.error("查询结果不唯一,返回多条记录: SQL : {},args:{}", originalSql, stmt.parameters);
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
        var sql = new StringBuilder();
        var countSql = new StringBuilder();

        var fieldSet = arguments.getFields();
        var fields = CollectionUtils.isEmpty(fieldSet) ? "*"
                : String.join(StringUtils.COMMA_SEPARATE, fieldSet);
        sql.append("SELECT ");
        countSql.append("SELECT ");
        if (arguments.isDistinct()) {
            sql.append("DISTINCT ");
            countSql.append("DISTINCT ");
        }
        sql.append(fields);
        sql.append(" FROM ");
        var countField = arguments.getCountField();
        countSql.append("COUNT(").append(StringUtils.isEmpty(countField) ? "*" : countField).append(") FROM ");

        sql.append(arguments.getFrom());
        countSql.append(arguments.getFrom());

        var parameters = new ArrayList<>();
        var condition = arguments.getConditions();
        if (null != condition) {
            var conditionSql = condition.toSqlString(parameters);
            if (StringUtils.isNotEmpty(conditionSql)) {
                sql.append(" WHERE ");
                sql.append(conditionSql);
                countSql.append(" WHERE ");
                countSql.append(conditionSql);
            }
        }
        var groupBys = arguments.getGroupBy();
        if (CollectionUtils.isNotEmpty(groupBys)) {
            var groupBySql = String.join(StringUtils.COMMA_SEPARATE, groupBys);
            sql.append(" GROUP BY ").append(groupBySql);
            countSql.append(" GROUP BY ").append(groupBySql).append(") result_");
            countSql.insert(0, "SELECT COUNT(*) FROM (");
        }
        sql.append(OrderUtils.toOrderSql(arguments.getOrders()));
        return new SelectStatement(sql, countSql, parameters);
    }

    private static class SelectStatement {

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

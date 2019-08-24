package com.hk.commons.configstore;

import com.hk.commons.util.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * 使用数据库存储
 *
 * @author kevin
 * @date 2019-8-21 9:26
 */
@Slf4j
@RequiredArgsConstructor
public class DataSourceConfigStorage<T extends ConfigID> implements ConfigStorage<T> {

    private final Lazy<DataSource> dataSource = Lazy.of(() -> SpringContextHolder.getBean(DataSource.class));

    private String findAllSql;

    private String findByIdSql;

    private final Set<String> columns = new LinkedHashSet<>();

    private final Map<String, String> fields = new HashMap<>();

    private final Class<T> clazz;

    private final String tableName;

    private final String idColumn;

    private final String columnString;

    @Setter
    private String order;

    /**
     * @param tableName 表名
     * @param idColumn  id 列名
     * @param clazz     类型
     * @param columns   其它列
     */
    public DataSourceConfigStorage(String tableName,
                                   String idColumn, Class<T> clazz, String columns) {
        this.tableName = tableName;
        this.idColumn = idColumn;
        this.clazz = clazz;
        this.columns.add(idColumn);
        this.columns.addAll(ArrayUtils.asArrayList(StringUtils.splitByComma(columns)));
        this.columnString = StringUtils.collectionToDelimitedString(this.columns, ",");
        for (String column : this.columns) {
            fields.put(column, StringUtils.lineToSmallHump(column));
        }
    }

    private String getFindAllSql() {
        if (null == findAllSql) {
            StringBuilder sb = new StringBuilder("SELECT ");
            sb.append(columnString).append(" FROM ").append(tableName);
            if (StringUtils.isNotEmpty(this.order)) {
                sb.append(" ORDER BY ").append(this.order).append(" ASC");
            }
            findAllSql = sb.toString();
        }
        return findAllSql;
    }

    private String getFindByIdSql() {
        if (null == findByIdSql) {
            findByIdSql = String.format("SELECT %s FROM %s WHERE %s = ?", columnString, tableName, idColumn);
        }
        return findByIdSql;
    }

    @Override
    @SneakyThrows
    public List<T> getAll() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.get().getConnection();
            String findAllSql = getFindAllSql();
            preparedStatement = connection.prepareStatement(findAllSql);
            log.debug("execute getAll Query: {}", findAllSql);
            resultSet = preparedStatement.executeQuery();
            List<T> result = new ArrayList<>();
            BeanWrapper beanWrapper;
            while (resultSet.next()) {
                beanWrapper = BeanWrapperUtils.createBeanWrapper(clazz);
                result.add(fullPropertyValues(beanWrapper, resultSet));
            }
            return result;
        } finally {
            ConnectionUtils.close(resultSet, preparedStatement, connection);
        }
    }

    @SneakyThrows
    private T fullPropertyValues(BeanWrapper beanWrapper, ResultSet resultSet) {
        for (String column : columns) {
            String fieldName = fields.get(column);
            if (StringUtils.isNotEmpty(fieldName)) {
                beanWrapper.setPropertyValue(fieldName, resultSet.getObject(column));
            }
        }
        return clazz.cast(beanWrapper.getWrappedInstance());
    }

    @Override
    @SneakyThrows
    public Optional<T> findById(Serializable id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.get().getConnection();
            String findByIdSql = getFindByIdSql();
            preparedStatement = connection.prepareStatement(findByIdSql);
            log.debug("executeQuery: {}", findByIdSql);
            preparedStatement.setObject(1, id);
            resultSet = preparedStatement.executeQuery();
            T data = null;
            if (resultSet.next()) {
                data = fullPropertyValues(BeanWrapperUtils.createBeanWrapper(clazz), resultSet);
            }
            return Optional.ofNullable(data);
        } finally {
            ConnectionUtils.close(resultSet, preparedStatement, connection);
        }
    }
}

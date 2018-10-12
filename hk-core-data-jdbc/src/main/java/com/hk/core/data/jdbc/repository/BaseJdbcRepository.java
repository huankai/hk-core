package com.hk.core.data.jdbc.repository;

import com.hk.commons.util.*;
import com.hk.core.data.commons.utils.OrderUtils;
import com.hk.core.data.jdbc.JdbcSession;
import com.hk.core.data.jdbc.SelectArguments;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.data.jdbc.query.SimpleCondition;
import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.*;

/**
 * @author: sjq-278
 * @date: 2018-10-11 13:21
 */
public class BaseJdbcRepository<T, ID> extends SimpleJdbcRepository<T, ID> implements JdbcRepository<T, ID> {

    private JdbcSession jdbcSession;

    @NonNull
    private final PersistentEntity<T, ? extends PersistentProperty> entity;

    public BaseJdbcRepository(JdbcAggregateOperations entityOperations, PersistentEntity<T, ?> entity) {
        super(entityOperations, entity);
        this.entity = entity;
    }

    private JdbcSession getJdbcSession() {
        if (null == jdbcSession) {
            jdbcSession = SpringContextHolder.getBean(JdbcSession.class);
        }
        return jdbcSession;
    }

    @Override
    public List<T> findAll(T t, Order... orders) {
        Class<T> type = entity.getType();
        PersistentProperty<?> idProperty = entity.getRequiredIdProperty();
        Iterable<? extends PersistentProperty> persistentProperties = entity.getPersistentProperties(Column.class);
        Map<String, String> propertyColumns = new LinkedHashMap<>();
        propertyColumns.put(idProperty.getName(), idProperty.getName());
        persistentProperties.forEach(item -> propertyColumns.put(item.getName(), ((Column) item.getRequiredAnnotation(Column.class)).name()));

        SelectArguments selectArguments = new SelectArguments();
        selectArguments.setFrom(entity.getRequiredAnnotation(Table.class).name());
        selectArguments.setOrders(ArrayUtils.asList(orders));
        selectArguments.setFields(new HashSet<>(propertyColumns.values()));
        CompositeCondition conditions = selectArguments.getConditions();
        Map<String, Object> propertyMap = BeanUtils.beanToMap(t, "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate");
        for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
            String column = propertyColumns.get(entry.getKey());
            if (null != column) {
                conditions.addCondition(new SimpleCondition(column, entry.getValue()));
            }
        }
        return getJdbcSession().queryForList(selectArguments, false, type).getResult();
    }

    @Override
    public QueryPage<T> queryForPage(QueryModel<T> query) {
        Class<T> type = entity.getType();
        PersistentProperty<?> idProperty = entity.getRequiredIdProperty();
        Iterable<? extends PersistentProperty> persistentProperties = entity.getPersistentProperties(Column.class);
        Map<String, String> propertyColumns = new LinkedHashMap<>();
        propertyColumns.put(idProperty.getName(), idProperty.getName());
        persistentProperties.forEach(item -> propertyColumns.put(item.getName(), ((Column) item.getRequiredAnnotation(Column.class)).name()));

        SelectArguments selectArguments = new SelectArguments();
        selectArguments.setFrom(entity.getRequiredAnnotation(Table.class).name());
        selectArguments.setOrders(query.getOrders());
        selectArguments.setFields(new HashSet<>(propertyColumns.values()));
        CompositeCondition conditions = selectArguments.getConditions();
        Map<String, Object> propertyMap = BeanUtils.beanToMap(query.getParam(), "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate");
        for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
            String column = propertyColumns.get(entry.getKey());
            if (null != column) {
                conditions.addCondition(new SimpleCondition(column, entry.getValue()));
            }
        }
        return getJdbcSession().queryForPage(selectArguments, type);
    }

    @Override
    public long count(T t) {
        PersistentProperty<?> idProperty = entity.getRequiredIdProperty();
        Iterable<? extends PersistentProperty> persistentProperties = entity.getPersistentProperties(Column.class);
        Map<String, String> propertyColumns = new LinkedHashMap<>();
        propertyColumns.put(idProperty.getName(), idProperty.getName());
        persistentProperties.forEach(item -> propertyColumns.put(item.getName(), ((Column) item.getRequiredAnnotation(Column.class)).name()));
        SelectArguments selectArguments = new SelectArguments();
        selectArguments.setFrom(entity.getRequiredAnnotation(Table.class).name());
        selectArguments.setFields(new HashSet<>(propertyColumns.values()));
        selectArguments.setCountField(idProperty.getName());
        CompositeCondition conditions = selectArguments.getConditions();
        Map<String, Object> propertyMap = BeanUtils.beanToMap(t, "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate");
        for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
            String column = propertyColumns.get(entry.getKey());
            if (null != column) {
                conditions.addCondition(new SimpleCondition(column, entry.getValue()));
            }
        }
        return getJdbcSession().queryForCount(selectArguments);
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        PersistentProperty<?> idProperty = entity.getRequiredIdProperty();
        Iterable<? extends PersistentProperty> persistentProperties = entity.getPersistentProperties(Column.class);
        Map<String, String> propertyColumns = new LinkedHashMap<>();
        propertyColumns.put(idProperty.getName(), idProperty.getName());
        persistentProperties.forEach(item -> propertyColumns.put(item.getName(), ((Column) item.getRequiredAnnotation(Column.class)).name()));
        SelectArguments selectArguments = new SelectArguments();
        selectArguments.setFrom(entity.getRequiredAnnotation(Table.class).name());
        selectArguments.setFields(new HashSet<>(propertyColumns.values()));

        selectArguments.setOrders(OrderUtils.toOrderList(sort));
        return getJdbcSession().queryForList(selectArguments, false, entity.getType()).getResult();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        PersistentProperty<?> idProperty = entity.getRequiredIdProperty();
        Iterable<? extends PersistentProperty> persistentProperties = entity.getPersistentProperties(Column.class);
        Map<String, String> propertyColumns = new LinkedHashMap<>();
        propertyColumns.put(idProperty.getName(), idProperty.getName());
        persistentProperties.forEach(item -> propertyColumns.put(item.getName(), ((Column) item.getRequiredAnnotation(Column.class)).name()));
        SelectArguments selectArguments = new SelectArguments();
        selectArguments.setFrom(entity.getRequiredAnnotation(Table.class).name());
        selectArguments.setFields(new HashSet<>(propertyColumns.values()));
        selectArguments.setCountField(idProperty.getName());
        selectArguments.setStartRowIndex(pageable.getPageNumber());
        selectArguments.setPageSize(pageable.getPageSize());
        selectArguments.setOrders(OrderUtils.toOrderList(pageable.getSort()));
        QueryPage<T> page = getJdbcSession().queryForPage(selectArguments, entity.getType());
        return new PageImpl<>(page.getData(), pageable, page.getTotalRow());
    }
}

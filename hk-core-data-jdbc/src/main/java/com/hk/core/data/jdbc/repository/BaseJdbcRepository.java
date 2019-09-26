package com.hk.core.data.jdbc.repository;

import com.hk.commons.util.*;
import com.hk.core.data.commons.utils.OrderUtils;
import com.hk.core.data.jdbc.exception.EntityNotFoundException;
import com.hk.core.jdbc.DeleteArguments;
import com.hk.core.jdbc.JdbcSession;
import com.hk.core.jdbc.SelectArguments;
import com.hk.core.jdbc.query.CompositeCondition;
import com.hk.core.jdbc.query.ConditionQueryModel;
import com.hk.core.jdbc.query.SimpleCondition;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.query.QueryModel;
import lombok.NonNull;
import org.springframework.data.domain.*;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.util.Lazy;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author kevin
 * @date 2018-10-11 13:21
 */
public class BaseJdbcRepository<T, ID> extends SimpleJdbcRepository<T, ID> implements JdbcRepository<T, ID> {

    @NonNull
    private final RelationalPersistentEntity<T> persistentEntity;

    private Lazy<JdbcSession> jdbcSession = Lazy.of(() -> SpringContextHolder.getBean(JdbcSession.class));

//    private Lazy<PersistentEntityMetadata> persistentEntityMetadata = Lazy.of(() -> SpringContextHolder.getBean(PersistentEntityMetadata.class));

    public BaseJdbcRepository(JdbcAggregateOperations entityOperations, RelationalPersistentEntity<T> persistentEntity) {
        super(entityOperations, persistentEntity);
        this.persistentEntity = persistentEntity;
    }

    @Override
    public ListResult<T> findAll(T t, Order... orders) {
        SelectArguments arguments = new SelectArguments();
        fillSelectArguments(arguments, t);
        arguments.setOrders(ArrayUtils.asArrayList(orders));
        return jdbcSession.get().queryForList(arguments, false, persistentEntity.getType());
    }

    private void fillSelectArguments(SelectArguments arguments, T entity) {
        arguments.setFrom(persistentEntity.getTableName());
        Map<String, String> columnList = new LinkedHashMap<>();
        persistentEntity.forEach(item -> columnList.put(item.getName(), item.getColumnName()));
        arguments.setFields(columnList.values());
        CompositeCondition conditions = arguments.getConditions();
        Map<String, Object> propertyMap = BeanUtils.beanToMap(entity);
        for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
            if (columnList.containsKey(entry.getKey()) && entry.getValue() != null) {
                conditions.addConditions(new SimpleCondition(columnList.get(entry.getKey()), entry.getValue()));
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public final T updateByIdSelective(T t) {
        if (t instanceof Persistable) {
            Persistable<ID> persistable = (Persistable<ID>) t;
            AssertUtils.isTrueWithI18n(!persistable.isNew(), "update.id.notEmpty");
            T find = getById(persistable.getId());
            BeanUtils.copyNotNullProperties(t, find);
            return save(find);
        }
        throw new IllegalArgumentException("不能识别的实体");
    }

    @Override
    public QueryPage<T> queryForPage(QueryModel<T> query) {
        SelectArguments arguments = new SelectArguments();
        fillSelectArguments(arguments, query.getParam());
        arguments.setCountField(persistentEntity.getIdColumn());
        arguments.setOrders(query.getOrders());
        arguments.setStartRowIndex(query.getStartRowIndex());
        arguments.setPageSize(query.getPageSize());
        return jdbcSession.get().queryForPage(arguments, persistentEntity.getType());
    }

    @Override
    public QueryPage<T> queryForPage(ConditionQueryModel query) {
        SelectArguments arguments = new SelectArguments();
        fillSelectArguments(arguments, null);
        arguments.setConditions(query.getParam());
        arguments.setCountField(persistentEntity.getIdColumn());
        arguments.setOrders(query.getOrders());
        arguments.setStartRowIndex(query.getStartRowIndex());
        arguments.setPageSize(query.getPageSize());
        return jdbcSession.get().queryForPage(arguments, persistentEntity.getType());
    }

    @Override
    public QueryPage<T> queryForPage(SelectArguments arguments) {
        fillSelectArguments(arguments, null);
        arguments.setCountField(persistentEntity.getIdColumn());
        return jdbcSession.get().queryForPage(arguments, persistentEntity.getType());
    }

    @Override
    public T getById(ID id) throws EntityNotFoundException {
        AssertUtils.notNull(id, "id 不能为 null");
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Optional<T> findOne(T t) {
        SelectArguments selectArguments = new SelectArguments();
        fillSelectArguments(selectArguments, t);
        return jdbcSession.get().queryForOne(selectArguments, persistentEntity.getType());
    }

    @Override
    public Optional<T> findOne(CompositeCondition condition) {
        SelectArguments arguments = new SelectArguments();
        fillSelectArguments(arguments, null);
        arguments.setConditions(condition);
        return jdbcSession.get().queryForOne(arguments, persistentEntity.getType());
    }

    @Override
    public ListResult<T> findAll(CompositeCondition condition, Collection<String> groupBys, Order... orders) {
        SelectArguments arguments = new SelectArguments();
        fillSelectArguments(arguments, null);
        arguments.setConditions(condition);
        arguments.setGroupBy(groupBys);
        arguments.setOrders(ArrayUtils.asArrayList(orders));
        return jdbcSession.get().queryForList(arguments, false, persistentEntity.getType());
    }

    @Override
    public boolean delete(CompositeCondition conditions) {
        return jdbcSession.get().delete(new DeleteArguments(persistentEntity.getTableName(), conditions));
    }

    @Override
    public long count(T t) {
        SelectArguments selectArguments = new SelectArguments();
        fillSelectArguments(selectArguments, t);
        selectArguments.setCountField(persistentEntity.getIdColumn());
        return jdbcSession.get().queryForCount(selectArguments);
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        SelectArguments selectArguments = new SelectArguments();
        fillSelectArguments(selectArguments, null);
        selectArguments.setOrders(OrderUtils.toOrderList(sort));
        return jdbcSession.get().queryForList(selectArguments, false, persistentEntity.getType()).getResult();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        SelectArguments selectArguments = new SelectArguments();
        fillSelectArguments(selectArguments, null);
        selectArguments.setCountField(persistentEntity.getIdColumn());
        selectArguments.setStartRowIndex(pageable.getPageNumber());
        selectArguments.setPageSize(pageable.getPageSize());
        selectArguments.setOrders(OrderUtils.toOrderList(pageable.getSort()));
        QueryPage<T> page = jdbcSession.get().queryForPage(selectArguments, persistentEntity.getType());
        return new PageImpl<>(page.getData(), pageable, page.getTotalRow());
    }
}

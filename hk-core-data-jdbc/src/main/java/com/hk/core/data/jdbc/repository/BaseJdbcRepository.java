package com.hk.core.data.jdbc.repository;

import com.hk.commons.util.*;
import com.hk.core.data.commons.utils.OrderUtils;
import com.hk.core.data.jdbc.DeleteArguments;
import com.hk.core.data.jdbc.JdbcSession;
import com.hk.core.data.jdbc.SelectArguments;
import com.hk.core.data.jdbc.exception.EntityNotFoundException;
import com.hk.core.data.jdbc.metadata.PersistentEntityInfo;
import com.hk.core.data.jdbc.metadata.PersistentEntityMetadata;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.data.jdbc.query.SimpleCondition;
import com.hk.core.page.ListResult;
import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import lombok.NonNull;
import org.springframework.data.domain.*;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author: sjq-278
 * @date: 2018-10-11 13:21
 */
public class BaseJdbcRepository<T, ID> extends SimpleJdbcRepository<T, ID> implements JdbcRepository<T, ID> {

    private JdbcSession jdbcSession;

    @NonNull
    private final PersistentEntity<T, ? extends PersistentProperty> entity;

    private PersistentEntityMetadata persistentEntityMetadata;

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

    private PersistentEntityMetadata getPersistentEntityMetadata() {
        if (null == persistentEntityMetadata) {
            persistentEntityMetadata = SpringContextHolder.getBean(PersistentEntityMetadata.class);
        }
        return persistentEntityMetadata;
    }

    @Override
    public ListResult<T> findAll(T t, Order... orders) {
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        SelectArguments selectArguments = new SelectArguments();
        fillSelectArguments(selectArguments, persistentEntityInfo, t);
        selectArguments.setOrders(ArrayUtils.asArrayList(orders));
        return getJdbcSession().queryForList(selectArguments, false, entity.getType());
    }

    private void fillSelectArguments(SelectArguments arguments, PersistentEntityInfo persistentEntityInfo, T t) {
        arguments.setFrom(persistentEntityInfo.getTableName());
        arguments.setFields(persistentEntityInfo.getPropertyColumns().values());
        CompositeCondition conditions = arguments.getConditions();
        Map<String, Object> propertyMap = BeanUtils.beanToMap(t);
        for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
            if (ArrayUtils.noContains(persistentEntityInfo.getIgnoreConditionFields(), entry.getKey())) {
                String column = persistentEntityInfo.getPropertyColumns().get(entry.getKey());
                if (null != column) {
                    conditions.addCondition(new SimpleCondition(column, entry.getValue()));
                }
            }
        }
    }

    @Override
    public long count() {
        return count(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final T updateByIdSelective(T t) {
        if (t instanceof Persistable) {
            Persistable<ID> persistable = (Persistable<ID>) t;
            AssertUtils.isTrue(!persistable.isNew(), "更新主键值不能为空");
            T find = getById(persistable.getId());
            BeanUtils.copyNotNullProperties(t, find);
            return save(find);
        }
        throw new IllegalArgumentException("不能识别的实体");
    }

    @Override
    public QueryPage<T> queryForPage(QueryModel<T> query) {
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        SelectArguments selectArguments = new SelectArguments();
        fillSelectArguments(selectArguments, persistentEntityInfo, query.getParam());
        selectArguments.setCountField(persistentEntityInfo.getIdField());
        selectArguments.setOrders(query.getOrders());
        return getJdbcSession().queryForPage(selectArguments, entity.getType());
    }

    @Override
    public QueryPage<T> queryForPage(SelectArguments arguments) {
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        arguments.setFrom(persistentEntityInfo.getTableName());
        if (CollectionUtils.isEmpty(arguments.getFields())) {
            arguments.setFields(new LinkedHashSet<>(persistentEntityInfo.getPropertyColumns().values()));
        }
        arguments.setCountField(persistentEntityInfo.getIdField());
        return getJdbcSession().queryForPage(arguments, entity.getType());
    }

    @Override
    public T getById(ID id) throws EntityNotFoundException {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public ListResult<T> findAll(CompositeCondition condition, Set<String> groupBys, Order... orders) {
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        SelectArguments selectArguments = new SelectArguments();
        fillSelectArguments(selectArguments, persistentEntityInfo, null);
        selectArguments.setConditions(condition);
        selectArguments.setGroupBy(groupBys);
        selectArguments.setOrders(ArrayUtils.asArrayList(orders));
        return getJdbcSession().queryForList(selectArguments, false, entity.getType());
    }

    @Override
    public boolean delete(CompositeCondition conditions) {
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        return getJdbcSession().delete(new DeleteArguments(persistentEntityInfo.getTableName(), conditions));
    }

    @Override
    public long count(T t) {
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        SelectArguments selectArguments = new SelectArguments();
        fillSelectArguments(selectArguments, persistentEntityInfo, t);
        selectArguments.setCountField(persistentEntityInfo.getIdField());
        return getJdbcSession().queryForCount(selectArguments);
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        SelectArguments selectArguments = new SelectArguments();
        fillSelectArguments(selectArguments, persistentEntityInfo, null);
        selectArguments.setOrders(OrderUtils.toOrderList(sort));
        return getJdbcSession().queryForList(selectArguments, false, entity.getType()).getResult();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        SelectArguments selectArguments = new SelectArguments();
        fillSelectArguments(selectArguments, persistentEntityInfo, null);
        selectArguments.setCountField(persistentEntityInfo.getIdField());
        selectArguments.setStartRowIndex(pageable.getPageNumber());
        selectArguments.setPageSize(pageable.getPageSize());
        selectArguments.setOrders(OrderUtils.toOrderList(pageable.getSort()));
        QueryPage<T> page = getJdbcSession().queryForPage(selectArguments, entity.getType());
        return new PageImpl<>(page.getData(), pageable, page.getTotalRow());
    }
}

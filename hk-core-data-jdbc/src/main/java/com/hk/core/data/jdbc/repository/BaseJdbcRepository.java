package com.hk.core.data.jdbc.repository;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.SpringContextHolder;
import com.hk.core.data.commons.utils.OrderUtils;
import com.hk.core.data.jdbc.JdbcSession;
import com.hk.core.data.jdbc.PersistentEntityInfo;
import com.hk.core.data.jdbc.PersistentEntityMetadata;
import com.hk.core.data.jdbc.SelectArguments;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.data.jdbc.query.SimpleCondition;
import com.hk.core.page.ListResult;
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
        Class<T> type = entity.getType();
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        SelectArguments selectArguments = new SelectArguments();
        selectArguments.setOrders(ArrayUtils.asList(orders));
        fillSelectArguments(selectArguments, persistentEntityInfo, t);
        return getJdbcSession().queryForList(selectArguments, false, type);
    }

    private void fillSelectArguments(SelectArguments arguments, PersistentEntityInfo persistentEntityInfo, T t) {
        arguments.setFrom(persistentEntityInfo.getTableName());
        arguments.setFields(new LinkedHashSet<>(persistentEntityInfo.getPropertyColumns().values()));
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
    public QueryPage<T> queryForPage(QueryModel<T> query) {
        Class<T> type = entity.getType();
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        SelectArguments selectArguments = new SelectArguments();
        selectArguments.setOrders(query.getOrders());
        selectArguments.setCountField(persistentEntityInfo.getIdField());
        fillSelectArguments(selectArguments, persistentEntityInfo, query.getParam());
        return getJdbcSession().queryForPage(selectArguments, type);
    }

    @Override
    public QueryPage<T> queryForPage(SelectArguments arguments) {
        Class<T> type = entity.getType();
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        arguments.setFrom(persistentEntityInfo.getTableName());
        if (CollectionUtils.isEmpty(arguments.getFields())) {
            arguments.setFields(new LinkedHashSet<>(persistentEntityInfo.getPropertyColumns().values()));
        }
        arguments.setCountField(persistentEntityInfo.getIdField());
        return getJdbcSession().queryForPage(arguments, type);
    }

    @Override
    public ListResult<T> findAll(CompositeCondition condition, Set<String> groupBys, Order... orders) {
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        SelectArguments selectArguments = new SelectArguments();
        fillSelectArguments(selectArguments, persistentEntityInfo, null);
        selectArguments.setConditions(condition);
        selectArguments.setGroupBy(groupBys);
        selectArguments.setOrders(ArrayUtils.asList(orders));
        return getJdbcSession().queryForList(selectArguments, false, entity.getType());
    }

    @Override
    public long count(T t) {
        PersistentEntityInfo persistentEntityInfo = getPersistentEntityMetadata().getPersistentEntityInfo(entity);
        SelectArguments selectArguments = new SelectArguments();
        selectArguments.setCountField(persistentEntityInfo.getIdField());
        fillSelectArguments(selectArguments, persistentEntityInfo, t);
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
        selectArguments.setCountField(persistentEntityInfo.getIdField());
        selectArguments.setStartRowIndex(pageable.getPageNumber());
        selectArguments.setPageSize(pageable.getPageSize());
        selectArguments.setOrders(OrderUtils.toOrderList(pageable.getSort()));
        fillSelectArguments(selectArguments, persistentEntityInfo, null);
        QueryPage<T> page = getJdbcSession().queryForPage(selectArguments, entity.getType());
        return new PageImpl<>(page.getData(), pageable, page.getTotalRow());
    }
}

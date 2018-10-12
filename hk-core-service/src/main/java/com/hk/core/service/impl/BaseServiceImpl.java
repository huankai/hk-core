package com.hk.core.service.impl;

import com.hk.commons.util.*;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.data.commons.utils.OrderUtils;
import com.hk.core.data.jdbc.JdbcSession;
import com.hk.core.data.jdbc.SelectArguments;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.data.jdbc.query.SimpleCondition;
import com.hk.core.exception.ServiceException;
import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.service.BaseService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Service CRUD操作
 *
 * @param <T>
 * @author: kevin
 * @date: 2017年9月27日下午2:16:24
 */
@Transactional(rollbackFor = {Throwable.class})
public abstract class BaseServiceImpl<T extends Persistable<ID>, ID extends Serializable> implements BaseService<T, ID> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

//    private DomainInfo domainInfo;

    @Autowired
    private SecurityContext securityContext;

//    @Autowired
//    private JdbcSession jdbcSession;

    protected final UserPrincipal getPrincipal() {
        return securityContext.getPrincipal();
    }

    /**
     * 返回 BaseRepository
     *
     * @return BaseRepository
     */
    protected abstract PagingAndSortingRepository<T, ID> getBaseRepository();

    @Override
    public void deleteById(ID id) {
        getBaseRepository().deleteById(id);
    }

    @Override
    public void delete(T entity) {
        getBaseRepository().delete(entity);
    }

    @Override
    public void delete(Iterable<T> entities) {
        getBaseRepository().deleteAll(entities);
    }

    @Override
    public T insert(T t) {
        return getBaseRepository().save(t);
    }

    @Override
    public Iterable<T> batchInsert(Iterable<T> entities) {
        return getBaseRepository().saveAll(entities);
    }

    @Override
    public Optional<T> findById(ID id) {
        return getBaseRepository().findById(id);
    }

    @Override
    public Iterable<T> findAll(Order... orders) {
        return getBaseRepository().findAll(OrderUtils.toSort(orders));
    }

//    @Override
//    public List<T> findAll(T t, Order... orders) {
//        SelectArguments selectArguments = new SelectArguments();
//        DomainInfo domainInfo = getDomainInfo();
//        selectArguments.setFrom(domainInfo.tableName);
//        selectArguments.setOrders(ArrayUtils.asList(orders));
//        selectArguments.setFields(new LinkedHashSet<>(domainInfo.propertyColumns.values()));
//        CompositeCondition conditions = selectArguments.getConditions();
//        Map<String, Object> propertyMap = BeanUtils.beanToMap(t);
//        for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
//            conditions.addCondition(new SimpleCondition(domainInfo.propertyColumns.get(entry.getKey()), entry.getValue()));
//        }
//        return jdbcSession.queryForList(selectArguments, false, domainInfo.clazz).getResult();
//    }

    @Override
    public Iterable<T> findByIds(Iterable<ID> ids) {
        return getBaseRepository().findAllById(ids);
    }

//    @Override
//    public QueryPage<T> queryForPage(QueryModel<T> query) {
//        SelectArguments selectArguments = new SelectArguments();
//        DomainInfo domainInfo = getDomainInfo();
//        selectArguments.setFields(new LinkedHashSet<>(domainInfo.propertyColumns.values()));
//        selectArguments.setCountField(domainInfo.id);
//        selectArguments.setFrom(domainInfo.tableName);
//        selectArguments.setOrders(query.getOrders());
//        selectArguments.setPageSize(query.getPageSize());
//        selectArguments.setStartRowIndex(query.getStartRowIndex());
//        CompositeCondition conditions = selectArguments.getConditions();
//        Map<String, Object> propertyMap = BeanUtils.beanToMap(query.getParam());
//        for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
//            conditions.addCondition(new SimpleCondition(domainInfo.propertyColumns.get(entry.getKey()), entry.getValue()));
//        }
//        return jdbcSession.queryForPage(selectArguments, domainInfo.clazz);
//    }

    @Override
    public boolean existsById(ID id) {
        return getBaseRepository().existsById(id);
    }

    @Override
    public boolean exists(T t) {
        return count(t) > 0;
    }

    @Override
    public long count() {
        return getBaseRepository().count();
    }

//    @Override
//    public long count(T t) {
//        throw new ServiceException("未实现");
//    }

    @Override
    public T updateById(T t) {
        AssertUtils.isTrue(!t.isNew(), "更新主键值不能为空");
        return getBaseRepository().save(t);
    }
//
//    @Override
//    public T updateByIdSelective(T t) {
//        throw new ServiceException("未实现");
//    }

//    @SuppressWarnings("unchecked")
//    private DomainInfo getDomainInfo() {
//        if (null == domainInfo) {
//            ResolvableType resolvableType = ResolvableType.forClass(getClass());
//            Class<T> domainClass = (Class<T>) resolvableType.getSuperType().getGeneric(0).resolve();
//            Table table = domainClass.getDeclaredAnnotation(Table.class);
//            List<Field> id = FieldUtils.getFieldsListWithAnnotation(domainClass, Id.class);
//            if (CollectionUtils.isEmpty(id)) {
//                id = FieldUtils.getFieldsListWithAnnotation(domainClass, org.springframework.data.annotation.Id.class);
//            }
//            if (CollectionUtils.isEmpty(id)) {
//                throw new ServiceException("不能获取主键字段");
//            }
//            List<Field> fields = FieldUtils.getFieldsListWithAnnotation(domainClass, Column.class);
//            Map<String, String> columns = new LinkedHashMap<>();
//            for (Field field : fields) {
//                columns.put(field.getName(), field.getAnnotation(Column.class).name());
//            }
//            this.domainInfo = new DomainInfo(domainClass, id.get(0).getName(), table.name(), columns);
//        }
//        return domainInfo;
//    }
//
//    protected final Class<T> getDomainInfoClass() {
//        return getDomainInfo().clazz;
//    }
//
//    @AllArgsConstructor
//    private class DomainInfo {
//
//        private Class<T> clazz;
//
//        private String id;
//
//        private String tableName;
//
//        private Map<String, String> propertyColumns;
//    }

    /**
     * <pre>
     *     在保存或更新实体之前
     *     可设置参数默认值，验证数据有效性
     * </pre>
     *
     * @param entity entity
     */
    protected T saveBefore(T entity) throws ServiceException {
        return entity;
    }

}
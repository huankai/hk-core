package com.hk.core.service.impl;

import com.hk.commons.util.*;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.domain.TreePersistable;
import com.hk.core.query.*;
import com.hk.core.query.jdbc.JdbcSession;
import com.hk.core.query.jdbc.ListResult;
import com.hk.core.query.jdbc.SelectArguments;
import com.hk.core.repository.BaseRepository;
import com.hk.core.service.BaseService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基本Service CRUD操作
 *
 * @param <T>
 * @author huangkai
 * @date 2017年9月27日下午2:16:24
 */
@Slf4j
@Transactional(rollbackFor = {Throwable.class})
public abstract class BaseServiceImpl<T extends Persistable<PK>, PK extends Serializable> implements BaseService<T, PK> {

    /**
     * 返回 BaseRepository
     *
     * @return
     */
    protected abstract BaseRepository<T, PK> getBaseRepository();

    /**
     * 表名
     */
    private String tableName;

    private Class<T> entityClass;

    /**
     * 所以有  @Id 和 @Column修饰的字段名
     */
    private Set<String> allColumnSet;

    /**
     * 获取登陆的用户信息
     */
    @Autowired
    @Getter
    protected SecurityContext securityContext;

    @Autowired
    private JdbcSession jdbcSession;

    /**
     * 获取表名
     *
     * @return 表名
     */
    protected final String getTableName() {
        if (StringUtils.isEmpty(tableName)) {
            tableName = AnnotationUtils.findAnnotation(getEntityClass(), Table.class).name();
        }
        return tableName;
    }

    protected final Class<T> getEntityClass() {
        if (null == entityClass) {
            this.entityClass = (Class<T>) ClassUtils.getGenericType(getClass(), 0);
        }
        return entityClass;
    }

    /**
     * 获取表所有字段，包含id字段
     *
     * @return
     */
    protected final Set<String> getAllColumnSet() {
        if (null == allColumnSet) {
            Class<T> clazz = getEntityClass();
            List<Field> idFieldList = FieldUtils.getFieldsListWithAnnotation(clazz, Id.class);
            Set<String> fieldSet = idFieldList.stream().map(Field::getName).collect(Collectors.toSet());

            List<Field> columnFieldList = FieldUtils.getFieldsListWithAnnotation(clazz, Column.class);
            Set<String> fields = columnFieldList.stream().map(field -> field.getAnnotation(Column.class).name()).collect(Collectors.toSet());
            fieldSet.addAll(fields);
            allColumnSet = Collections.unmodifiableSet(fieldSet);
        }
        return allColumnSet;
    }

    /**
     * 获取当前登陆的用户
     *
     * @return
     */
    protected final UserPrincipal getUserPrincipal() {
        return securityContext.getPrincipal();
    }

    @Override
    public <S extends T> S saveOrUpdate(S entity) {
        return getBaseRepository().save(saveBefore(entity));
    }

    /**
     * <pre>
     *     在保存或更新实体之前
     *     可设置参数默认值，验证数据有效性
     * </pre>
     *
     * @param entity
     */
    protected <S extends T> S saveBefore(S entity) {
        if (entity instanceof TreePersistable) {
            TreePersistable<T> treeEntity = (TreePersistable<T>) entity;
            if (treeEntity.getParent() == null) {
                treeEntity.setParent(entity);
            }
        }
        return entity;
    }

    @Override
    public <S extends T> List<S> saveOrUpdate(Iterable<S> entities) {
        entities.forEach(this::saveBefore);
        return getBaseRepository().save(entities);
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return getBaseRepository().saveAndFlush(saveBefore(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public T findOne(PK id) {
        return getBaseRepository().findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public <S extends T> S findOne(S t) {
        return getBaseRepository().findOne(Example.of(checkNull(t), ofExampleMatcher()));
    }

    @Override
    @Transactional(readOnly = true)
    public <S extends T> List<S> findAll(S t) {
        return findAll(t, (Order[]) null);
    }

    @Override
    @Transactional(readOnly = true)
    public <S extends T> List<S> findAll(S t, Order... orders) {
        Sort sort = null;
        if (ArrayUtils.isNotEmpty(orders)) {
            List<Sort.Order> orderList = Arrays.stream(orders).filter(Objects::nonNull).map(Order::toSpringJpaOrder).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(orderList)) {
                sort = new Sort(orderList);
            }
        }
        return getBaseRepository().findAll(Example.of(checkNull(t), ofExampleMatcher()), sort);
    }

    @Override
    @Transactional(readOnly = true)
    public T getOne(PK id) {
        return getBaseRepository().getOne(id);
    }

    @Override
    public void flush() {
        getBaseRepository().flush();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(PK id) {
        return getBaseRepository().exists(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return getBaseRepository().findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Iterable<PK> ids) {
        return getBaseRepository().findAll(ids);
    }

    /**
     * <pre>
     *
     * 查询之前，检查参数是否为null
     * 使用 jpa查询的参数不能为空
     * </pre>
     *
     * @param t   要查询的条件
     * @param <S>
     * @return t
     */
    protected <S extends T> S checkNull(S t) {
        if (null == t) {
            t = (S) BeanUtils.instantiate(getEntityClass());
        }
        return t;
    }

    /**
     * 使用JPA 设置查询条件匹配
     *
     * @return ExampleMatcher
     */
    protected ExampleMatcher ofExampleMatcher() {
        return ExampleMatcher.matching();
    }

    @Override
    @Transactional(readOnly = true)
    public QueryPageable<T> queryForPage(QueryModel query) {
        if (query instanceof JdbcQueryModel) {
            return queryForPage((JdbcQueryModel) query);
        }
        T param = null;
        if (query instanceof JpaQueryModel) {
            param = ((JpaQueryModel<T>) query).getParams();
        }
        List<Sort.Order> orderList = query.getSortOrderList();
        Page<T> page = getBaseRepository().findAll(Example.of(checkNull(param), ofExampleMatcher()),
                new PageRequest(query.getJpaStartRowIndex(), query.getPageSize(),
                        CollectionUtils.isEmpty(orderList) ? null : new Sort(orderList)));
        return new SimpleQueryResult<>(query, page.getTotalElements(), page.getContent());
    }

    @SuppressWarnings("unchecked")
    private QueryPageable<T> queryForPage(JdbcQueryModel query) {
        SelectArguments arguments = query.toSelectArguments();
        arguments.setFields(getAllColumnSet());
        arguments.setFrom(getTableName());
        ListResult<T> result = jdbcSession.queryForList(arguments, !query.isPaging(), getEntityClass());
        return new SimpleQueryResult<>(query, result.getTotalRowCount(), result.getResult());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return getBaseRepository().count();
    }

    @Override
    @Transactional(readOnly = true)
    public long count(T t) {
        return getBaseRepository().count(Example.of(checkNull(t), ofExampleMatcher()));
    }

    @Override
    public void delete(PK id) {
        getBaseRepository().delete(deleteBefore(getOne(id)));
    }

    @Override
    public void delete(T entity) {
        getBaseRepository().delete(deleteBefore(entity));
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        entities.forEach(this::delete);
    }

    /**
     * 删除之前
     *
     * @param entity 删除的实体
     * @param <S>
     * @return
     */
    protected <S extends T> S deleteBefore(S entity) {
        return entity;
    }
}
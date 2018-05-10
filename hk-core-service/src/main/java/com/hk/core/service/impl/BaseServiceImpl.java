package com.hk.core.service.impl;

import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.ClassUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.FieldUtils;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.query.JdbcQueryModel;
import com.hk.core.query.QueryModel;
import com.hk.core.query.QueryPageable;
import com.hk.core.query.SimpleQueryResult;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基本Service CRUD操作
 *
 * @param <T>
 * @author huangkai
 * @date 2017年9月27日下午2:16:24
 */
@Slf4j
@Transactional(readOnly = true, rollbackFor = {Throwable.class})
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
    @Getter
    private final String tableName;

    @Getter
    private final Class<T> clazz;

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

    public BaseServiceImpl() {
        this.clazz = (Class<T>) ClassUtils.getGenericType(getClass(), 0);
        List<Field> idFieldList = FieldUtils.getFieldsListWithAnnotation(clazz, Id.class);
        Set<String> fieldSet = idFieldList.stream().map(Field::getName).collect(Collectors.toSet());

        List<Field> columnFieldList = FieldUtils.getFieldsListWithAnnotation(clazz, Column.class);
        Set<String> fields = columnFieldList.stream().map(field -> field.getAnnotation(Column.class).name()).collect(Collectors.toSet());
        fieldSet.addAll(fields);
        allColumnSet = Collections.unmodifiableSet(fields);
        tableName = AnnotationUtils.findAnnotation(clazz, Table.class).name();
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
    @Transactional
    public <S extends T> S saveOrUpdate(S entity) {
        return getBaseRepository().save(entity);
    }

    @Override
    @Transactional
    public <S extends T> List<S> saveOrUpdate(Iterable<S> entities) {
        return getBaseRepository().save(entities);
    }

    @Override
    @Transactional
    public <S extends T> S saveAndFlush(S entity) {
        return getBaseRepository().saveAndFlush(entity);
    }

    @Override
    public T findOne(PK id) {
        return getBaseRepository().findOne(id);
    }

    @Override
    public <S extends T> S findOne(S t) {
        return getBaseRepository().findOne(getExample(t));
    }

    @Override
    public <S extends T> List<S> findAll(S t) {
        return getBaseRepository().findAll(getExample(t));
    }

    @Override
    public T getOne(PK id) {
        return getBaseRepository().getOne(id);
    }

    @Override
    @Transactional
    public void flush() {
        getBaseRepository().flush();
    }

    @Override
    public boolean exists(PK id) {
        return getBaseRepository().exists(id);
    }

    @Override
    public List<T> findAll() {
        return getBaseRepository().findAll();
    }

    @Override
    public List<T> findAll(Iterable<PK> ids) {
        return getBaseRepository().findAll(ids);
    }

    /**
     * @param t
     * @return
     */
    private <S extends T> Example<S> getExample(S t) {
        if (null == t) {
            t = (S) BeanUtils.instantiate(clazz);
        }
        return Example.of(t, ofExampleMatcher());
    }

    /**
     * 查询条件匹配
     *
     * @return
     */
    protected ExampleMatcher ofExampleMatcher() {
        return ExampleMatcher.matching();
    }

    @Override
    public QueryPageable<T> queryForPage(QueryModel query) {
        if (query instanceof JdbcQueryModel) {
            return queryForPage((JdbcQueryModel) query);
        }
        List<Sort.Order> orderList = query.getSortOrderList();
        Page<T> page = getBaseRepository().findAll(
                new PageRequest(query.getJpaStartRowIndex(), query.getPageSize(), CollectionUtils.isEmpty(orderList) ? null : new Sort(orderList)));
        return new SimpleQueryResult<>(query, page.getTotalElements(), page.getContent());
    }

    @SuppressWarnings("unchecked")
    protected QueryPageable<T> queryForPage(JdbcQueryModel query) {
        SelectArguments arguments = query.toSelectArguments();
        arguments.setFields(allColumnSet);
        arguments.setFrom(tableName);
        ListResult<T> result = jdbcSession.queryForList(arguments, true, clazz);
        return new SimpleQueryResult<>(query, result.getTotalRowCount(), result.getResult());
    }

    @Override
    public long count() {
        return getBaseRepository().count();
    }

    @Override
    public long count(T t) {
        return getBaseRepository().count(getExample(t));
    }

    @Override
    @Transactional
    public void delete(PK id) {
        getBaseRepository().delete(id);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        getBaseRepository().delete(entity);
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends T> entities) {
        getBaseRepository().delete(entities);
    }

}

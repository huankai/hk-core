package com.hk.core.service.impl;

import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.ClassUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.query.JpaQueryModel;
import com.hk.core.query.QueryModel;
import com.hk.core.query.QueryPageable;
import com.hk.core.query.SimpleQueryResult;
import com.hk.core.repository.BaseRepository;
import com.hk.core.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

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
     * 获取登陆的用户信息
     */
    @Autowired
    protected SecurityContext securityContext;

    /**
     * 获取当前登陆的用户
     *
     * @return
     */
    protected final UserPrincipal getUserPrincipal() {
        return securityContext.getPrincipal();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#saveOrUpdate(java.lang.Object)
     */
    @Override
    @Transactional
    public <S extends T> S saveOrUpdate(S entity) {
        return getBaseRepository().save(entity);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#saveOrUpdate(java.lang.Iterable)
     */
    @Override
    @Transactional
    public <S extends T> List<S> saveOrUpdate(Iterable<S> entities) {
        return getBaseRepository().save(entities);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#saveAndFlush(java.lang.Object)
     */
    @Override
    @Transactional
    public <S extends T> S saveAndFlush(S entity) {
        return getBaseRepository().saveAndFlush(entity);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#findOne(java.io.Serializable)
     */
    @Override
    public T findOne(PK id) {
        return getBaseRepository().findOne(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#findOne(java.lang.Object)
     */
    @Override
    public <S extends T> S findOne(S t) {
        return getBaseRepository().findOne(getExample(t));
    }

    @Override
    public <S extends T> List<S> findAll(S t) {
        return getBaseRepository().findAll(getExample(t));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#getOne(java.io.Serializable)
     */
    @Override
    public T getOne(PK id) {
        return getBaseRepository().getOne(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#flush()
     */
    @Override
    @Transactional
    public void flush() {
        getBaseRepository().flush();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#exists(java.io.Serializable)
     */
    @Override
    public boolean exists(PK id) {
        return getBaseRepository().exists(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#findAll()
     */
    @Override
    public List<T> findAll() {
        return getBaseRepository().findAll();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#findAll(java.lang.Iterable)
     */
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
            t = BeanUtils.instantiate((Class<S>) ClassUtils.getGenericType(this.getClass(), 0));
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

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hk.core.service.BaseService#queryForPage(com.hk.core.query.QueryModel)
     */
    @Override
    public QueryPageable<T> queryForPage(QueryModel query) {
        List<Sort.Order> orderList = query.getSortOrderList();
        Page<T> page = getBaseRepository().findAll(
                new PageRequest(query.getJpaStartRowIndex(), query.getPageSize(), CollectionUtils.isEmpty(orderList) ? null : new Sort(orderList)));
        return new SimpleQueryResult<>(query, page.getTotalElements(), page.getContent());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hk.core.service.BaseService#queryForPage(com.hk.core.query.JpaQueryModel)
     */
    @Override
    public QueryPageable<T> queryForPage(JpaQueryModel<T> query) {
        List<Sort.Order> orderList = query.getSortOrderList();
        Page<T> page = getBaseRepository().findAll(getExample(query.getParams()),
                new PageRequest(query.getJpaStartRowIndex(), query.getPageSize(), CollectionUtils.isEmpty(orderList) ? null : new Sort(orderList)));
        return new SimpleQueryResult<>(query, page.getTotalElements(), page.getContent());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#count()
     */
    @Override
    public long count() {
        return getBaseRepository().count();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#count(java.lang.Object)
     */
    @Override
    public long count(T t) {
        return getBaseRepository().count(getExample(t));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#delete(java.io.Serializable)
     */
    @Override
    @Transactional
    public void delete(PK id) {
        getBaseRepository().delete(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hk.core.service.BaseService#delete(java.lang.Object)
     */
    @Override
    @Transactional
    public void delete(T entity) {
        getBaseRepository().delete(entity);
    }

    /*
     *
     */
    @Override
    @Transactional
    public void delete(Iterable<? extends T> entities) {
        getBaseRepository().delete(entities);
    }

}

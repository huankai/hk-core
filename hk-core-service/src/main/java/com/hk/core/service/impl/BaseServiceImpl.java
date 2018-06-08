package com.hk.core.service.impl;

import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.ObjectUtils;
import com.hk.core.data.commons.BaseDao;
import com.hk.core.data.commons.domain.TreePersistable;
import com.hk.core.data.commons.query.Order;
import com.hk.core.data.commons.query.QueryModel;
import com.hk.core.data.commons.query.QueryPage;
import com.hk.core.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Persistable;
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
@Transactional(rollbackFor = {Throwable.class})
public abstract class BaseServiceImpl<T extends Persistable<ID>, ID extends Serializable> implements BaseService<T, ID> {


    private Class<T> domainClass;

    /**
     * 返回 BaseRepository
     *
     * @return
     */
    protected abstract BaseDao<T, ID> getBaseDao();

    @Override
    public T saveOrUpdate(T entity) {
        return getBaseDao().insertOrUpdate(saveBefore(entity));
    }

    @Override
    public Iterable<T> saveOrUpdate(Iterable<T> entities) {
        return getBaseDao().batchInsert(entities);
    }

    @Override
    public T updateByIdSelective(T entity) {
        return getBaseDao().updateByIdSelective(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public T findOne(ID id) {
        return getBaseDao().findById(id);
    }

    @Override
    public T getOne(ID id) {
        return getBaseDao().getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public T findOne(T t) {
        return getBaseDao().findOne(Example.of(checkNull(t), ofExampleMatcher()));
    }

    protected ExampleMatcher ofExampleMatcher() {
        return ExampleMatcher.matching();
    }

    /**
     * Example 查询参数不能为null
     *
     * @param t
     * @return
     */
    private T checkNull(T t) {
        return ObjectUtils.defaultIfNull(t, BeanUtils.instantiate(getDomainClass()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(T t, Order... orders) {
        return getBaseDao().findAll(Example.of(checkNull(t), ofExampleMatcher()), orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return getBaseDao().findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<T> findByIds(Iterable<ID> ids) {
        return getBaseDao().findByIds(ids);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public QueryPage<T> queryForPage(QueryModel<T> query) {
        return getBaseDao().findByPage(Example.of(checkNull(query.getParam()), ofExampleMatcher()), query.getOrders(), query.getStartRowIndex(), query.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(ID id) {
        return getBaseDao().exists(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(T t) {
        return getBaseDao().exists(Example.of(checkNull(t), ofExampleMatcher()));
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return getBaseDao().count();
    }

    @Override
    @Transactional(readOnly = true)
    public long count(T t) {
        return getBaseDao().count(Example.of(t, ofExampleMatcher()));
    }

    /**
     * 直接删除
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(ID id) {
        return getBaseDao().deleteById(id);
    }

    @Override
    public boolean delete(T entity) {
        return getBaseDao().deleteEntity(deleteBefore(entity));
    }

    @Override
    public boolean deleteByIds(Iterable<ID> ids) {
        return getBaseDao().deleteByIds(ids);
    }

    @Override
    public boolean delete(Iterable<T> entities) {
        return getBaseDao().deleteEntities(entities);
    }

    /**
     * 删除之前
     *
     * @param entity 删除的实体
     * @return
     */
    protected T deleteBefore(T entity) {
        return entity;
    }

    /**
     * <pre>
     *     在保存或更新实体之前
     *     可设置参数默认值，验证数据有效性
     * </pre>
     *
     * @param entity
     */
    @SuppressWarnings("unchecked")
    protected T saveBefore(T entity) {
        if (entity instanceof TreePersistable) {
            TreePersistable<T, ID> treeEntity = (TreePersistable<T, ID>) entity;
            if (treeEntity.getParent() == null) {
                treeEntity.setParent(entity);
            }
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getDomainClass() {
        if (null == domainClass) {
            ResolvableType resolvableType = ResolvableType.forClass(getClass());
            domainClass = (Class<T>) resolvableType.getSuperType().getGeneric(0).resolve();
        }
        return domainClass;
    }
}
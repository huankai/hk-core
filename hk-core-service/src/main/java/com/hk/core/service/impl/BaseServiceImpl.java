package com.hk.core.service.impl;

import com.hk.core.data.commons.BaseDao;
import com.hk.core.data.commons.domain.TreePersistable;
import com.hk.core.data.commons.query.Order;
import com.hk.core.data.commons.query.QueryModel;
import com.hk.core.data.commons.query.QueryPage;
import com.hk.core.service.BaseService;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 返回 BaseRepository
     *
     * @return
     */
    protected abstract BaseDao<T, ID> getBaseDao();

    @Override
    public boolean saveOrUpdate(T entity) {
        return getBaseDao().insertOrUpdate(saveBefore(entity));
    }

    @Override
    public boolean saveOrUpdate(Iterable<T> entities) {
        entities.forEach(this::saveOrUpdate);
        return true;
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
        return getBaseDao().findOne(t);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(T t, Order... orders) {
        return getBaseDao().findAll(t, orders);
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
    public QueryPage<T> queryForPage(QueryModel query) {
        return getBaseDao().findByPage(query);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(ID id) {
        return getBaseDao().exists(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(T t) {
        return getBaseDao().exists(t);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return getBaseDao().count();
    }

    @Override
    @Transactional(readOnly = true)
    public long count(T t) {
        return getBaseDao().count(t);
    }

    /**
     * 直接删除
     *
     * @param id
     * @return
     */
    @Override
    public void deleteById(ID id) {
        getBaseDao().deleteById(id);
    }

    @Override
    public void delete(T entity) {
        getBaseDao().delete(deleteBefore(entity));
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        entities.forEach(this::delete);
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
    protected T saveBefore(T entity) {
        if (entity instanceof TreePersistable) {
            TreePersistable<T, ID> treeEntity = (TreePersistable<T, ID>) entity;
            if (treeEntity.getParent() == null) {
                treeEntity.setParent(entity);
            }
        }
        return entity;
    }
}
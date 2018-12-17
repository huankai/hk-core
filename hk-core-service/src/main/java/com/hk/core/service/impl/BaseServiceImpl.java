package com.hk.core.service.impl;

import com.hk.commons.util.AssertUtils;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.data.commons.utils.OrderUtils;
import com.hk.core.query.Order;
import com.hk.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

/**
 * Service CRUD操作
 *
 * @param <T>
 * @author kevin
 * @date 2017年9月27日下午2:16:24
 */
@Transactional(rollbackFor = {Throwable.class})
public abstract class BaseServiceImpl<T extends Persistable<ID>, ID extends Serializable> implements BaseService<T, ID> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SecurityContext securityContext;

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
        return getBaseRepository().save(saveBefore(t));
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

    @Override
    public Iterable<T> findByIds(Iterable<ID> ids) {
        return getBaseRepository().findAllById(ids);
    }

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

    @Override
    public T updateById(T t) {
        AssertUtils.isTrue(!t.isNew(), "更新主键值不能为空");
        return getBaseRepository().save(t);
    }

    /**
     * <pre>
     *     在保存或更新实体之前
     *     可设置参数默认值，验证数据有效性
     * </pre>
     *
     * @param entity entity
     */
    protected T saveBefore(T entity) {
        return entity;
    }

}
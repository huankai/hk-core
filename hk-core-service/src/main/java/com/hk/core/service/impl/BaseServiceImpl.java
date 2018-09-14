package com.hk.core.service.impl;

import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.ObjectUtils;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.data.jpa.repository.BaseRepository;
import com.hk.core.data.jpa.util.OrderUtils;
import com.hk.core.exception.ServiceException;
import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Persistable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Service CRUD操作
 *
 * @param <T>
 * @author kevin
 * @date: 2017年9月27日下午2:16:24
 */
@Transactional(rollbackFor = {Throwable.class})
public abstract class BaseServiceImpl<T extends Persistable<ID>, ID extends Serializable> implements BaseService<T, ID> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Class<T> domainClass;

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
    protected abstract BaseRepository<T, ID> getBaseRepository();

    @Override
    public void deleteById(ID id) {
        getBaseRepository().deleteById(id);
    }

    @Override
    public void deleteByIds(Collection<ID> ids) {
        ids.forEach(this::deleteById);
    }

    @Override
    public void delete(T entity) {
        getBaseRepository().delete(entity);
    }

    @Override
    public void delete(Collection<T> entities) {
        getBaseRepository().deleteAll(entities);
    }

    @Override
    public T insert(T t) {
        return getBaseRepository().save(saveBefore(t));
    }

    @Override
    public Collection<T> batchInsert(Collection<T> entities) {
        return getBaseRepository().saveAll(entities);
    }

    /**
     * Example 查询参数不能为null
     *
     * @param t t
     * @return t
     */
    private T checkNull(T t) {
        return ObjectUtils.defaultIfNull(t, BeanUtils.instantiateClass(getDomainClass()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findOne(ID id) {
        return getBaseRepository().findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public T getOne(ID id) {
        return getBaseRepository().getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findOne(T t) {
        return getBaseRepository().findOne(Example.of(checkNull(t), ofExampleMatcher()));
    }

    protected ExampleMatcher ofExampleMatcher() {
        return ExampleMatcher.matchingAll().withIgnoreNullValues();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(T t, Order... orders) {
        return getBaseRepository().findAll(Example.of(checkNull(t), ofExampleMatcher()), orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Order... orders) {
        return getBaseRepository().findAll(OrderUtils.toSort(orders));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<T> findByIds(Iterable<ID> ids) {
        return getBaseRepository().findAllById(ids);

    }

    @Override
    @Transactional(readOnly = true)
    public QueryPage<T> queryForPage(QueryModel<T> query) {
        return getBaseRepository().findByPage(Example.of(checkNull(query.getParam()), ofExampleMatcher()),
                query.getOrders(), query.getStartRowIndex(), query.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(ID id) {
        return getBaseRepository().existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(T t) {
        return getBaseRepository().exists(Example.of(checkNull(t), ofExampleMatcher()));
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

    @SuppressWarnings("unchecked")
    private Class<T> getDomainClass() {
        if (null == domainClass) {
            ResolvableType resolvableType = ResolvableType.forClass(getClass());
            domainClass = (Class<T>) resolvableType.getSuperType().getGeneric(0).resolve();
        }
        return domainClass;
    }

    @Override
    public T updateById(T t) {
        AssertUtils.isTrue(!t.isNew(), "Update Id must not be null");
        return getBaseRepository().save(t);
    }

    @Override
    public T updateByIdSelective(T t) {
        return getBaseRepository().updateByIdSelective(t);
    }

    @Override
    public Collection<T> batchUpdate(Collection<T> entities) {
        return getBaseRepository().saveAll(entities);
    }

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
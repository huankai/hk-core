package com.hk.core.service.jpa.impl;

import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.ObjectUtils;
import com.hk.core.data.jpa.repository.BaseJpaRepository;
import com.hk.core.query.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.service.impl.BaseServiceImpl;
import com.hk.core.service.jpa.JpaBaseService;
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
 * @author kevin
 * @date 2018-10-10 10:30
 */
public abstract class JpaServiceImpl<T extends Persistable<ID>, ID extends Serializable> extends BaseServiceImpl<T, ID> implements JpaBaseService<T, ID> {

    @Override
    protected abstract BaseJpaRepository<T, ID> getBaseRepository();

    private Class<T> domainClass;

    protected ExampleMatcher ofExampleMatcher() {
        return ExampleMatcher.matchingAll().withIgnoreNullValues();
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

    @SuppressWarnings(("unchecked"))
    private Class<T> getDomainClass() {
        if (null == domainClass) {
            ResolvableType resolvableType = ResolvableType.forClass(getClass());
            domainClass = (Class<T>) resolvableType.getSuperType().getGeneric(0).resolve();
        }
        return domainClass;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findOne(T t) {
        return getBaseRepository().findOne(Example.of(checkNull(t), ofExampleMatcher()));
    }

    @Override
    @Transactional(readOnly = true)
    public T getOne(ID id) {
        return getBaseRepository().getOne(id);
    }

    @Override
    public List<T> findAll(T t, Order... orders) {
        return getBaseRepository().findAll(Example.of(checkNull(t), ofExampleMatcher()), orders);
    }

    @Override
    public QueryPage<T> queryForPage(QueryModel<T> query) {
        return getBaseRepository().findByPage(Example.of(checkNull(query.getParam()), ofExampleMatcher()),
                query.getOrders(), query.getStartRowIndex(), query.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public long count(T t) {
        return getBaseRepository().count(Example.of(checkNull(t), ofExampleMatcher()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(T t) {
        return getBaseRepository().exists(Example.of(checkNull(t), ofExampleMatcher()));
    }

    @Override
    public List<T> batchUpdate(Collection<T> entities) {
        return getBaseRepository().saveAll(entities);
    }

    @Override
    public T updateByIdSelective(T t) {
        return getBaseRepository().updateByIdSelective(t);
    }

}

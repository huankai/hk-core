package com.hk.core.service.jpa.impl;

import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.ObjectUtils;
import com.hk.core.data.commons.utils.OrderUtils;
import com.hk.core.data.jpa.repository.BaseJpaRepository;
import com.hk.core.jdbc.query.ConditionQueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.page.SimpleQueryPage;
import com.hk.core.query.Order;
import com.hk.core.query.QueryModel;
import com.hk.core.service.impl.BaseServiceImpl;
import com.hk.core.service.jpa.JpaBaseService;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
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

    @SuppressWarnings("unchecked")
    private Class<T> getDomainClass() {
        if (null == domainClass) {
            ResolvableType resolvableType = ResolvableType.forClass(getClass());
            this.domainClass = (Class<T>) resolvableType.getSuperType().getGeneric(0).resolve();
        }
        return domainClass;
    }

    @Override
    public Optional<T> findOne(T t) {
        return getBaseRepository().findOne(Example.of(checkNull(t), ofExampleMatcher()));
    }

    @Override
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
    public QueryPage<T> queryForPage(ConditionQueryModel queryModel) {
        return getBaseRepository().queryForPage(queryModel);
    }

    @Override
    public long count(T t) {
        return getBaseRepository().count(Example.of(checkNull(t), ofExampleMatcher()));
    }

    @Override
    public boolean exists(T t) {
        return getBaseRepository().exists(Example.of(checkNull(t), ofExampleMatcher()));
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public List<T> batchUpdate(Collection<T> entities) {
        return getBaseRepository().saveAll(entities);
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public T updateByIdSelective(T t) {
        return getBaseRepository().updateByIdSelective(t);
    }

    @Override
    public Optional<T> findOne(Specification<T> spec) {
        return getBaseRepository().findOne(spec);
    }

    @Override
    public QueryPage<T> findAll(Specification<T> spec, int pageIndex, int pageSize, Order... orders) {
        Page<T> page = getBaseRepository().findAll(spec, PageRequest.of(pageIndex, pageSize, OrderUtils.toSort(orders)));
        return new SimpleQueryPage<>(page.getContent(), page.getTotalElements(), pageIndex, pageSize);
    }

    @Override
    public List<T> findAll(Specification<T> spec, Order... orders) {
        return getBaseRepository().findAll(spec, OrderUtils.toSort(orders));
    }

    @Override
    public long count(Specification<T> spec) {
        return getBaseRepository().count(spec);
    }
}

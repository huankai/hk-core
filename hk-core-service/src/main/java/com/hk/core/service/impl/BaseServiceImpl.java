package com.hk.core.service.impl;

import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.SpringContextHolder;
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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Service CRUD操作
 *
 * @param <T>
 * @author kevin
 * @date 2017年9月27日下午2:16:24
 */
public abstract class BaseServiceImpl<T extends Persistable<ID>, ID extends Serializable> implements BaseService<T, ID> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SecurityContext securityContext;

    protected final UserPrincipal getPrincipal() {
        return securityContext.getPrincipal();
    }

    /**
     * 国际化消息
     *
     * @param messageCode 国际化消息编号
     * @param args        国际化消息参数
     * @return 国际化消息
     */
    protected final String getMessage(String messageCode, Object... args) {
        return getMessage(messageCode, null, args);
    }

    /**
     * 国际化消息
     *
     * @param messageCode    国际化消息编号
     * @param defaultMessage 国际化默认消息
     * @param args           国际化消息参数
     * @return 国际化消息
     */
    protected final String getMessage(String messageCode, String defaultMessage, Object... args) {
        return SpringContextHolder.getMessageWithDefault(messageCode, defaultMessage, args);
    }

    /**
     * 返回 BaseRepository
     *
     * @return BaseRepository
     */
    protected abstract PagingAndSortingRepository<T, ID> getBaseRepository();

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void deleteById(ID id) {
        getBaseRepository().deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void delete(T entity) {
        getBaseRepository().delete(entity);
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void delete(Iterable<T> entities) {
        getBaseRepository().deleteAll(entities);
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public T insert(T t, Function<T, T> function) {
        return getBaseRepository().save(function.apply(t));
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public Iterable<T> batchInsert(Iterable<T> entities) {
        return getBaseRepository().saveAll(entities);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Objects.isNull(id) ? Optional.empty() : getBaseRepository().findById(id);
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
        return Objects.nonNull(id) && getBaseRepository().existsById(id);
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
    @Transactional(rollbackFor = {Throwable.class})
    public T updateById(T t, Function<T, T> function) {
        AssertUtils.isTrueWithI18n(!t.isNew(), "update.id.notEmpty");
        return getBaseRepository().save(function.apply(t));
    }

}

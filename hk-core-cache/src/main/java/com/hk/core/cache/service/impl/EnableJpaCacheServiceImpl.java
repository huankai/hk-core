package com.hk.core.cache.service.impl;

import com.hk.core.cache.service.CacheInvalidService;
import com.hk.core.service.jpa.JpaBaseService;
import com.hk.core.service.jpa.impl.JpaServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * //TODO 未实现 Service implementation Enable Cache.
 *
 * @author kevin
 * @date 2018-05-16 09:58
 * @see com.hk.core.cache.spring.FixUseSupperClassAnnotationParser
 * @see com.hk.core.cache.spring.FixUseSupperClassCacheOperationSource
 */
public abstract class EnableJpaCacheServiceImpl<T extends Persistable<PK>, PK extends Serializable> extends JpaServiceImpl<T, PK> implements JpaBaseService<T, PK>, CacheInvalidService {

    @Override
    @Cacheable(key = "'id'+#root.args[0]")
    public T getOne(PK pk) {
        return super.getOne(pk);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "'id'+#id"),
                    @CacheEvict(key = "'count'")
            }
    )
    public void deleteById(PK pk) {
        super.deleteById(pk);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(T entity) {
        super.delete(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(Iterable<T> entities) {
        super.delete(entities);
    }

    @Override
    @Cacheable(key = "'id'+#root.args[0]")
    public Optional<T> findOne(T t) {
        return super.findOne(t);
    }

    @Override
    @Cacheable(key = "'id'+#root.args[0]")
    public Optional<T> findById(PK pk) {
        return super.findById(pk);
    }

//    @Override
//    @Cacheable(key = "'id'+#root.args[0]")
//    public Iterable<T> findByIds(Iterable<PK> pks) {
//        return super.findByIds(pks);
//    }
//
//    @Override
//    @Cacheable(key = "'id'+#root.args")
//    public Iterable<T> findByIds(PK... pks) {
//        return super.findByIds(pks);
//    }

    @Override
    @Cacheable(key = "'count'")
    public long count() {
        return super.count();
    }

    @Override
    @CachePut(key = "'id'+#root.args[0].id")
    public T updateById(T t) {
        return super.updateById(t);
    }

    @Override
    @CachePut(key = "'id'+#root.args[0].id")
    public T updateById(T t, Function<T, T> function) {
        return super.updateById(t, function);
    }

    @Override
    @CachePut(key = "'id'+#root.args[0].id")
    public T updateByIdSelective(T t) {
        return super.updateByIdSelective(t);
    }

    @Override
    @Caching(put = @CachePut(key = "'id'+#root.args[0].id", condition = "#root.args[0].id != null"))
    public T insertOrUpdate(T t) {
        return super.insertOrUpdate(t);
    }

    @Caching(put = @CachePut(key = "'id'+#root.args[0].id", condition = "#root.args[0].id != null"))
    public T insertOrUpdateSelective(T t) {
        return super.insertOrUpdateSelective(t);
    }

    @Override
    @CacheEvict(allEntries = true)
    public List<T> insertOrUpdate(Collection<T> entities) {
        return super.insertOrUpdate(entities);
    }

    @Override
    @CacheEvict(allEntries = true)
    public List<T> insertOrUpdate(Collection<T> entities, Function<T, T> function) {
        return super.insertOrUpdate(entities, function);
    }

    @Override
    @CacheEvict(allEntries = true)
    public List<T> insertOrUpdateSelective(Collection<T> entities) {
        return super.insertOrUpdateSelective(entities);
    }

    @Override
    @CacheEvict(allEntries = true)
    public List<T> insertOrUpdateSelective(Collection<T> entities, Function<T, T> function) {
        return super.insertOrUpdateSelective(entities, function);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteByIds(Iterable<PK> pks) {
        super.deleteByIds(pks);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteByIds(@SuppressWarnings("unchecked") PK... pks) {
        super.deleteByIds(pks);
    }

    @Override
    @CacheEvict(allEntries = true)
    public List<T> batchUpdate(Collection<T> entities) {
        return super.batchUpdate(entities);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void cleanCache() {

    }

    @SuppressWarnings("unchecked")
    protected final EnableJpaCacheServiceImpl<T, PK> getCurrentProxy() {
        return (EnableJpaCacheServiceImpl<T, PK>) AopContext.currentProxy();
    }

}

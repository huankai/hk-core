package com.hk.core.cache.service.impl;

import com.hk.core.cache.service.JdbcCacheService;
import com.hk.core.jdbc.query.CompositeCondition;
import com.hk.core.service.jdbc.impl.JdbcServiceImpl;
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
 * Service implementation Enable Cache.
 *
 * @author kevin
 * @date 2018-05-16 09:58
 * @see com.hk.core.cache.spring.FixUseSupperClassAnnotationParser
 * @see com.hk.core.cache.spring.FixUseSupperClassCacheOperationSource
 */
public abstract class EnableJdbcCacheServiceImpl<T extends Persistable<ID>, ID extends Serializable> extends JdbcServiceImpl<T, ID> implements JdbcCacheService<T, ID> {

    @Override
    @Cacheable(key = "'id'+#root.args[0]")
    public T getById(ID id) {
        return super.getById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean delete(CompositeCondition conditions) {
        return super.delete(conditions);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "'id'+#id"),
                    @CacheEvict(key = "'count'")
            }
    )
    public void deleteById(ID id) {
        super.deleteById(id);
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
    public Optional<T> findById(ID id) {
        return super.findById(id);
    }

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
    public List<T> insertOrUpdateSelective(Collection<T> entities) {
        return super.insertOrUpdateSelective(entities);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteByIds(Iterable<ID> ids) {
        super.deleteByIds(ids);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteByIds(ID... ids) {
        super.deleteByIds(ids);
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

}

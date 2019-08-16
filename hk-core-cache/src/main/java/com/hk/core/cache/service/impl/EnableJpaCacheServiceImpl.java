package com.hk.core.cache.service.impl;

import com.hk.core.cache.service.JpaCacheService;
import com.hk.core.service.jpa.impl.JpaServiceImpl;
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
public abstract class EnableJpaCacheServiceImpl<T extends Persistable<ID>, ID extends Serializable> extends JpaServiceImpl<T, ID> implements JpaCacheService<T, ID> {

    /**
     * <pre>
     *
     * cacheNames/values: 指定缓存名称
     * key: 缓存数据时的 key，可能使用 spel 表达式，默认 key 为方法参数值
     * keyGenerator: 可以指定key 的生成器，与 属性 key 二选一即可。
     * cacheManager: 指定缓存管理器，或者指定 cacheResolver，二选一
     * cacheResolver:
     * condition: 符合条件的情况下才缓存， spel 表达式
     * unless: 否定缓存，当 unless 的条件为 true ，方法的返回值不缓存，与 condition 相反。可以获取到结果进行判断
     * sync: 是否使用异步模式
     *
     * </pre>
     *
     * @param pk
     * @return
     */
    @Override
    @Cacheable(key = "'id'+#root.args[0]")
    public T getOne(ID pk) {
        return super.getOne(pk);
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
    public Optional<T> findOne(T t) {
        return super.findOne(t);
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

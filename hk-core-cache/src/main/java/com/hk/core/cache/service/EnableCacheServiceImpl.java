package com.hk.core.cache.service;

import com.hk.core.service.impl.BaseServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

/**
 * Service implementation Enable Cache.
 *
 * @author: kevin
 * @date 2018-05-16 09:58
 * @see com.hk.core.cache.spring.FixUseSupperClassAnnotationParser
 * @see com.hk.core.cache.spring.FixUseSupperClassCacheOperationSource
 */
public abstract class EnableCacheServiceImpl<T extends Persistable<PK>, PK extends Serializable> extends BaseServiceImpl<T, PK> {

    /**
     * <p>
     * If the key(ID) exists in the cache,it is obtained directly from the cache.<br/>
     * Otherwise,query from the database and put the result in the cache.
     * <p>
     *
     * @param id id
     * @return
     */
    @Override
    @Cacheable(key = "'id'+#root.args[0]")
    public Optional<T> findOne(PK id) {
        return super.findOne(id);
    }

    @Override
    @Cacheable(key = "'id'+#root.args[0]")
    public T getOne(PK pk) {
        return super.getOne(pk);
    }

    /**
     * Caching count.
     *
     * @return
     */
    @Override
    @Cacheable(key = "'count'")
    public long count() {
        return super.count();
    }

    /**
     * 删除缓存
     *
     * @param entity
     * @return
     */
    @Override
    @CachePut(key = "'id'+#root.args[0].id")
    public T updateByIdSelective(T entity) {
        return super.updateByIdSelective(entity);
    }

    @Override
    @Caching(put = @CachePut(key = "'id'+#root.args[0].id", condition = "#root.args[0].id != null"))
    public T insertOrUpdate(T t) {
        return super.insertOrUpdate(t);
    }

    @Override
//    @Cacheable(key = "'id'+#result.id")
    public T insert(T t) {
        return super.insert(t);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Collection<T> insertOrUpdate(Collection<T> entities) {
        return super.insertOrUpdate(entities);
    }

    @Override
    @CachePut(key = "'id'+#root.args[0].id")
    public T updateById(T t) {
        return super.updateById(t);
    }

    /**
     * <p>
     * Delete data from the primary key and delete the data from the cache.
     * </p>
     *
     * @param id id
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "'id'+#id"),
                    @CacheEvict(key = "'count'")
            }
    )
    public void deleteById(PK id) {
        super.deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteByIds(Collection<PK> ids) {
        super.deleteByIds(ids);
    }

    /**
     * <p>
     * delete entity and Delete Cache.<br/>
     * When id != null,delete the single record of the specified ID and delete the cached count record.<br/>
     * Otherwise,It is impossible to determine how many records are deleted and empty the entity and count in the cache.
     * <p>
     *
     * @param entity entity
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "'id'+#root.args[0].id", condition = "#root.args[0].id != null"),
                    @CacheEvict(allEntries = true, condition = "#root.args[0].id == null"),
                    @CacheEvict(key = "'count'")
            }
    )
    public void delete(T entity) {
        super.delete(entity);
    }

    /**
     * <p>
     * delete List and Clear Cache.
     * </p>
     *
     * @param entities entities
     */
    @Override
    @CacheEvict(allEntries = true)
    public void delete(Collection<T> entities) {
        super.delete(entities);
    }

    /**
     * <p>
     * Get the current proxy object.
     * </p>
     * <p>
     * must be set @EnableAspectJAutoProxy(exposeProxy = true) to true.
     * </p>
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    protected final EnableCacheServiceImpl<T, PK> getCurrentProxy() {
        return (EnableCacheServiceImpl<T, PK>) AopContext.currentProxy();
    }

}

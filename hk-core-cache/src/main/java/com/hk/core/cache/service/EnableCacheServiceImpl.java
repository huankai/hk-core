package com.hk.core.cache.service;

import com.hk.core.service.impl.BaseServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * Service implementation Enable Cache.
 *
 * @author: huangkai
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
    @Cacheable(key = "'id'+#id")
    public T findOne(PK id) {
        return super.findOne(id);
    }

    @Override
    @CacheEvict(key = "'id'+#args[0].id", condition = "#args[0].id != null")
    public T saveOrUpdate(T entity) {
        return super.saveOrUpdate(entity);
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

    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "'id'+#root.args[0].id"),
                    @CacheEvict(key = "'count'")
            }
    )
    public T updateByIdSelective(T entity) {
        return super.updateByIdSelective(entity);
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
    public boolean deleteById(PK id) {
        return super.deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean deleteByIds(Iterable<PK> ids) {
        return super.deleteByIds(ids);
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
    public boolean delete(T entity) {
        return super.delete(entity);
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
    public boolean delete(Iterable<T> entities) {
        return super.delete(entities);
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

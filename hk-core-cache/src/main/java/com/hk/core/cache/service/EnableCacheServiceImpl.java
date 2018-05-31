package com.hk.core.cache.service;

import com.hk.core.service.impl.BaseServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;

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
    @Cacheable(key = "'id'+#id")
    public T findOne(PK id) {
        return super.findOne(id);
    }

    /**
     * <p>
     * If the key(ID) exists in the cache,it is obtained directly from the cache.<br/>
     * Otherwise,query from the database and put the result in the cache.
     * <p>
     *
     * @param id id
     * @return
     */
    @Cacheable(key = "'id'+#id")
    public T getOne(PK id) {
        return super.getOne(id);
    }

    /**
     * <p>
     * save or udpate.<br/>
     * Data will only be placed in cache when there is update.
     * </p>
     *
     * @param entity the save or update entity.
     * @param <S>
     * @return
     */
    @CacheEvict(key = "'id'+#root.args[0].id", condition = "#root.args[0].id != null")
    public <S extends T> S saveOrUpdate(S entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(key = "'id'+#root.args[0].id", condition = "#root.args[0].id != null")
    public boolean saveFlushOrUpdate(T t, boolean updateNullField) {
        return super.saveFlushOrUpdate(t, updateNullField);
    }

    /**
     * <p>
     * Batch storage,
     * Deleting the data in the cache.
     * </p>
     *
     * @param entities the save or update entities.
     * @param <S>
     * @return
     */
    @CacheEvict(allEntries = true)
    public <S extends T> List<S> saveOrUpdate(Iterable<S> entities) {
        return super.saveOrUpdate(entities);
    }

    /**
     * <p>
     * It is only when updated that the return value is placed in the cache.
     * </p>
     *
     * @param entity the save or update entity.
     * @param <S>
     * @return
     */
    @CacheEvict(key = "'id'+#root.args[0].id", condition = "#root.args[0].id != null")
    public <S extends T> S saveAndFlush(S entity) {
        return super.saveAndFlush(entity);
    }

    /**
     * Caching count.
     *
     * @return
     */
    @Cacheable(key = "'count'")
    public long count() {
        return super.count();
    }

    /**
     * <p>
     * Delete data from the primary key and delete the data from the cache.
     * </p>
     *
     * @param id id
     */
    @Caching(
            evict = {
                    @CacheEvict(key = "'id'+#id"),
                    @CacheEvict(key = "'count'")
            }
    )
    public void delete(PK id) {
        super.delete(id);
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
    @CacheEvict(allEntries = true)
    public void delete(Iterable<? extends T> entities) {
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
    protected final EnableCacheServiceImpl<T, PK> getCurrentProxy() {
        return (EnableCacheServiceImpl<T, PK>) AopContext.currentProxy();
    }

}

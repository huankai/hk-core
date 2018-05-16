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
 * @author: huangkai
 * @date 2018-05-16 09:58
 */
public abstract class EnableCacheServiceImpl<T extends Persistable<PK>, PK extends Serializable> extends BaseServiceImpl<T, PK> {


    /**
     * 如果key(id)在缓存中存在，直接从缓存中获取；
     * 如果key不存在，将返回结果放入缓存中
     *
     * @param id
     * @return
     */
    @Cacheable(key = "'id'+#id")
    public T findOne(PK id) {
        return super.findOne(id);
    }

    /**
     * 如果key(id)在缓存中存在，直接从缓存中获取；
     * 如果key不存在，将返回结果放入缓存中
     *
     * @param id id
     * @return
     */
    @Cacheable(key = "'id'+#id")
    public T getOne(PK id) {
        return super.getOne(id);
    }

    /**
     * <pre>
     *  保存或更新一条记录
     *  只在有更新的时候才会将数据放入缓存中
     * </pre>
     *
     * @param entity the save or update entity.
     * @param <S>
     * @return
     */
    @CacheEvict(key = "'id'+#root.args[0].id", condition = "#root.args[0].id != null")
    public <S extends T> S saveOrUpdate(S entity) {
        return super.saveOrUpdate(entity);
    }

    /**
     * <pre>
     *
     * 批量保存
     * 将缓存中的数据清空
     * </pre>
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
     * <pre>
     * 只有在更新的时候，才会将返回值放入在缓存中.
     * </pre>
     *
     * @param entity the save or update entity.
     * @param <S>
     * @return
     */
    @CacheEvict(key = "'id'+#root.args[0].id", condition = "#root.args[0].id != null")
    public <S extends T> S saveAndFlush(S entity) {
        return super.saveAndFlush(entity);
    }

    @Cacheable(key = "'count'")
    public long count() {
        return super.count();
    }

    /**
     * <pre>
     *     根据id删除，并删除缓存中存在的id记录
     * </pre>
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
     * <pre>
     *  删除缓存:
     *  当id != null时，删除指定id的单条记录，同时删除 count 记录
     *  当id == null时，无法确定删除有多少条记录，将所有entity都删除，同时删除 count 记录
     *
     * </pre>
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
     * delete List
     *
     * @param entities entities
     */
    @CacheEvict(allEntries = true)
    public void delete(Iterable<? extends T> entities) {
        super.delete(entities);
    }

    /**
     * <pre>
     * 如果是子类调用
     * 获取当前代理对象
     * 必须设置 @EnableAspectJAutoProxy(exposeProxy = true) exposeProxy 为true
     * </pre>
     *
     * @return
     * @see com.hk.core.cache.config.FixUseSupperClassAutoConfiguration
     */
    protected final EnableCacheServiceImpl<T, PK> getCurrentProxy() {
        return (EnableCacheServiceImpl<T, PK>) AopContext.currentProxy();
    }

}

package com.hk.core.service.impl;

import com.hk.core.query.QueryModel;
import com.hk.core.query.QueryPageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;

/**
 * 提供缓存功能的实现
 * 可以在子类中标注 {@link org.springframework.cache.annotation.CacheConfig} 实现缓存
 *
 * @author: huangkai
 * @date 2018-04-19 09:25
 * @see com.hk.core.cache.spring.FixUseSupperClassAnnotationParser
 * @see com.hk.core.cache.spring.FixUseSupperClassCacheOperationSource
 * @see com.hk.core.cache.spring.FixUseSupperClassFallbackCacheOperationSource
 */
public abstract class EnableCacheServiceImpl<T extends Persistable<PK>, PK extends Serializable> extends BaseServiceImpl<T, PK> {

    @Override
    @Cacheable(key = "'id'+#id")
    public T findOne(PK id) {
        return super.findOne(id);
    }

    @Override
    @Cacheable(key = "'id'+#id")
    public T getOne(PK id) {
        return super.getOne(id);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "'id:'+#result.id"),
                    @CacheEvict(key = "'all'"),
                    @CacheEvict(key = "'count'")
            }
    )
    public <S extends T> S saveOrUpdate(S entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public <S extends T> List<S> saveOrUpdate(Iterable<S> entities) {
        return super.saveOrUpdate(entities);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "'id:'+#result.id"),
                    @CacheEvict(key = "'all'"),
                    @CacheEvict(key = "'count'")
            }
    )
    public <S extends T> S saveAndFlush(S entity) {
        return super.saveAndFlush(entity);
    }

    @Override
    public <S extends T> S findOne(S t) {
        return super.findOne(t);
    }

    @Override
    public <S extends T> List<S> findAll(S t) {
        return super.findAll(t);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "'id:'+#result.id"),
                    @CacheEvict(key = "'all'"),
                    @CacheEvict(key = "'count'")
            }
    )
    public void flush() {
        super.flush();
    }

    @Override
    @Cacheable(key = "'id'+#id")
    public boolean exists(PK id) {
        return super.exists(id);
    }

    @Override
    public List<T> findAll() {
        return super.findAll();
    }

    @Override
    public List<T> findAll(Iterable<PK> ids) {
        return super.findAll(ids);
    }

    @Override
    public QueryPageable<T> queryForPage(QueryModel query) {
        return super.queryForPage(query);
    }

    @Override
    @Cacheable(key = "'count'")
    public long count() {
        return super.count();
    }

    @Override
    public long count(T t) {
        return super.count(t);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "'id:'+#pk"),
                    @CacheEvict(key = "'all'"),
                    @CacheEvict(key = "'count'")
            }
    )
    public void delete(PK id) {
        super.delete(id);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "'id:'+#pk"),
                    @CacheEvict(key = "'all'"),
                    @CacheEvict(key = "'count'")
            }
    )
    public void delete(T entity) {
        super.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        super.delete(entities);
    }
}

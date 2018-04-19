package com.hk.core.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;

/**
 * 提供缓存功能的实现
 *
 * @author: huangkai
 * @date 2018-04-19 09:25
 */
public abstract class EnableCacheServiceImpl<T extends Persistable<PK>, PK extends Serializable> extends BaseServiceImpl<T, PK> {

    @Override
    @Cacheable(key = "#id")
    public T findOne(PK id) {
        return super.findOne(id);
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return super.saveAndFlush(entity);
    }

    @Override
    public <S extends T> S saveOrUpdate(S entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    public <S extends T> List<S> saveOrUpdate(Iterable<S> entities) {
        return super.saveOrUpdate(entities);
    }

    @Override
    @Caching(evict = {@CacheEvict(key = "'id:'+#id")})
    public void delete(PK id) {
        super.delete(id);
    }

    @Override
    public void delete(T entity) {
        super.delete(entity);
    }
}

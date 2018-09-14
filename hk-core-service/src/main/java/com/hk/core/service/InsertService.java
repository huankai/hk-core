package com.hk.core.service;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author: kevin
 * @date: 2018-07-04 09:40
 */
public interface InsertService<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * 实体保存
     *
     * @param t t
     * @return T
     */
    T insert(T t);

    /**
     * 实体批量保存
     *
     * @param entities entities
     * @return Collection
     */
    Collection<T> batchInsert(Collection<T> entities);

}

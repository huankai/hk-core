package com.hk.core.service;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;

/**
 * @author kevin
 * @date 2018-07-04 09:40
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
     * @return {@link Iterable}
     */
    Iterable<T> batchInsert(Iterable<T> entities);

}

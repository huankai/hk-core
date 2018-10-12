package com.hk.core.service;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    Iterable<T> batchInsert(Iterable<T> entities);

}

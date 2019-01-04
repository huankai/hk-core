package com.hk.core.service;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.function.Function;

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
    default T insert(T t) {
        return insert(t, Function.identity());
    }

    T insert(T t, Function<T, T> function);

    /**
     * 实体批量保存
     *
     * @param entities entities
     * @return {@link Iterable}
     */
    Iterable<T> batchInsert(Iterable<T> entities);

}

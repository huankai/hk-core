package com.hk.core.data.commons;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * @author: kevin
 * @date 2018-6-6 22:45
 */
public interface Delete<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    boolean deleteById(ID id);

    /**
     * 不为空的条件删除
     *
     * @param t
     * @return
     */
    <S extends T> boolean deleteEntity(T t);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    default boolean deleteByIds(Iterable<ID> ids) {
        ids.forEach(this::deleteById);
        return true;
    }

    boolean deleteEntities(Iterable<T> entities);
}


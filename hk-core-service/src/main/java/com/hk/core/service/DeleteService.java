package com.hk.core.service;

import com.hk.commons.util.ArrayUtils;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * @author kevin
 * @date 2018-07-04 09:47
 */
public interface DeleteService<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * @param id id
     */
    void deleteById(ID id);

    /**
     * @param ids ids
     */
    default void deleteByIds(Iterable<ID> ids) {
        if (null != ids) {
            ids.forEach(this::deleteById);
        }
    }

    /**
     * @param ids ids
     */
    default void deleteByIds(@SuppressWarnings("unchecked") ID... ids) {
        deleteByIds(ArrayUtils.asArrayList(ids));
    }

    /**
     * 会根据 实体 id 删除 ， 实体id 不能为空
     *
     * @param entity entity
     */
    void delete(T entity);

    /**
     * 会根据 实体 id 删除，实体id 不能为空
     *
     * @param entities entities
     */
    void delete(Iterable<T> entities);
}

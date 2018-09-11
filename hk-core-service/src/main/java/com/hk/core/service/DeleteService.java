package com.hk.core.service;

import com.hk.commons.util.ArrayUtils;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author: kevin
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
    void deleteByIds(Collection<ID> ids);

    /**
     * @param ids ids
     */
    @SuppressWarnings("unchecked")
    default void deleteByIds(ID... ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            deleteByIds(Arrays.asList(ids));
        }
    }

    /**
     * @param entity entity
     */
    void delete(T entity);

    /**
     * @param entities entities
     */
    void delete(Collection<T> entities);
}

package com.hk.core.service;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.AssertUtils;
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
     * @param id
     * @return
     */
    void deleteById(ID id);

    /**
     * @param ids
     */
    void deleteByIds(Collection<ID> ids);

    /**
     * @param ids
     * @return
     */
    default void deleteByIds(ID... ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            deleteByIds(Arrays.asList(ids));
        }
    }

    /**
     * @param entity
     */
    void delete(T entity);

    /**
     * @param entities
     */
    void delete(Collection<T> entities);
}

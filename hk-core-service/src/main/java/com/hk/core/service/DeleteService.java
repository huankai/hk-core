package com.hk.core.service;

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
    boolean deleteById(ID id);

    /**
     * @param ids
     */
    boolean deleteByIds(Collection<ID> ids);

    /**
     * @param ids
     * @return
     */
    default boolean deleteByIds(ID... ids) {
        AssertUtils.notNull(ids, "Array Id must not be null");
        return deleteByIds(Arrays.asList(ids));
    }

    /**
     * @param entity
     */
    boolean delete(T entity);

    /**
     * @param entities
     */
    boolean delete(Iterable<T> entities);
}

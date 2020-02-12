package com.hk.core.service;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
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
        DeleteService<T, ID> proxy = Service.currentProxy(this);
        if (CollectionUtils.isNotEmpty(ids)) {
            ids.forEach(proxy::deleteById);
        }
    }

    /**
     * @param ids ids
     */
    default void deleteByIds(ID... ids) {
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

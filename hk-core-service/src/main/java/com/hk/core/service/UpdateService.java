package com.hk.core.service;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: kevin
 * @date 2018-07-04 09:40
 */
public interface UpdateService<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * 根据主键更新
     *
     * @param t
     * @return
     */
    T updateById(T t);

    /**
     * 根据主键只更新不为空的字段
     *
     * @param t
     * @return
     */
    T updateByIdSelective(T t);

    /**
     * 批量更新
     *
     * @param entities
     * @return
     */
    default Collection<T> batchUpdate(Collection<T> entities) {
        List<T> result = new ArrayList<>(entities.size());
        entities.forEach(entity -> result.add(updateById(entity)));
        return result;
    }

}

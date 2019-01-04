package com.hk.core.service;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author kevin
 * @date 2018-07-04 09:40
 */
public interface UpdateService<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * 根据主键更新
     *
     * @param t t
     * @return t
     */
    default T updateById(T t) {
        return updateById(t, Function.identity());
    }

    /**
     * 更新
     *
     * @param t        t
     * @param function function
     * @return T
     */
    T updateById(T t, Function<T, T> function);

    /**
     * 根据主键只更新不为空的字段
     *
     * @param t t
     * @return t
     */
    T updateByIdSelective(T t);

    /**
     * 批量更新
     *
     * @param entities entities
     * @return {@link List}
     */
    default List<T> batchUpdate(Collection<T> entities) {
        List<T> result = new ArrayList<>(entities.size());
        entities.forEach(entity -> result.add(updateById(entity)));
        return result;
    }

}

package com.hk.core.service;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * 基本CRUD操作
 *
 * @param <T>
 * @param <ID>
 * @author kevin
 * @date 2017年9月27日下午5:04:48
 */
public interface BaseService<T extends Persistable<ID>, ID extends Serializable>
        extends InsertService<T, ID>, UpdateService<T, ID>, DeleteService<T, ID>, SelectService<T, ID> {

    /**
     * 保存或更新，在更新时，只更新不为 null 的字段
     *
     * @param t T
     * @return T
     */
    default T insertOrUpdateSelective(T t) {
        return t.isNew() ? insert(t) : updateByIdSelective(t);
    }

    /**
     * 保存或更新
     *
     * @param t T
     * @return T
     */
    default T insertOrUpdate(T t) {
        return t.isNew() ? insert(t) : updateById(t);
    }

    /**
     * 批量保存或更新
     *
     * @param entities entitys
     * @return {@link List}
     */
    default List<T> insertOrUpdate(Collection<T> entities) {
        return insertOrUpdate(entities, Function.identity());
    }

    /**
     * 批量保存或更新
     *
     * @param entities entitys
     * @param function 每条记录执行器
     * @return {@link List}
     */
    default List<T> insertOrUpdate(Collection<T> entities, Function<T, T> function) {
        List<T> result = new ArrayList<>(entities.size());
        entities.forEach(entity -> result.add(insertOrUpdate(function.apply(entity))));
        return result;
    }

    /**
     * 批量保存或更新，在更新时，只更新不为 null 的字段
     *
     * @param entities entitys
     * @param function 每条记录执行器
     * @return {@link List}
     */
    default List<T> insertOrUpdateSelective(Collection<T> entities) {
        return insertOrUpdateSelective(entities, Function.identity());
    }


    /**
     * 批量保存或更新，在更新时，只更新不为 null 的字段
     *
     * @param entities entitys
     * @param function 每条记录执行器
     * @return {@link List}
     */
    default List<T> insertOrUpdateSelective(Collection<T> entities, Function<T, T> function) {
        List<T> result = new ArrayList<>(entities.size());
        entities.forEach(entity -> result.add(insertOrUpdateSelective(function.apply(entity))));
        return result;
    }
}

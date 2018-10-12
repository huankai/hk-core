package com.hk.core.service;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 基本CRUD操作
 *
 * @param <T>
 * @param <ID>
 * @author: kevin
 * @date: 2017年9月27日下午5:04:48
 */
public interface BaseService<T extends Persistable<ID>, ID extends Serializable>
        extends InsertService<T, ID>, UpdateService<T, ID>, DeleteService<T, ID>, SelectService<T, ID> {

    default T insertOrUpdate(T t) {
        return t.isNew() ? insert(t) : updateById(t);
    }

    default List<T> insertOrUpdate(Iterable<T> entities) {
        List<T> result = new ArrayList<>();
        entities.forEach(entity -> result.add(insertOrUpdate(entity)));
        return result;
    }
}

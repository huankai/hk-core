package com.hk.core.data.commons;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author: kevin
 * @date 2018-6-6 22:35
 */
public interface Insert<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * 保存
     *
     * @param t
     * @return
     */
    T insert(T t);

    /**
     * 批量保存
     *
     * @param iterable
     * @return
     */
    Collection<T> batchInsert(Collection<T> iterable);


}

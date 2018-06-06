package com.hk.core.data.commons;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author huangkai
 * @date 2018-6-6 22:45
 */
public interface Delete<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(ID id);

    /**
     * 不为空的条件删除
     *
     * @param t
     * @return
     */
    int delete(T t);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    default int delete(Collection<ID> ids) {
        ids.forEach(this::deleteById);
        return ids.size();
    }
}


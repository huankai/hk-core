package com.hk.core.data.commons;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @author: huangkai
 * @date 2018-06-07 14:31
 */
@NoRepositoryBean
public interface BaseDao<T extends Persistable<ID>, ID extends Serializable> extends Insert<T, ID>, Delete<T, ID>, Update<T, ID>, Select<T, ID> {

    default boolean insertOrUpdate(T t) {
        return t.isNew() ? insert(t) : updateByPrimaryKey(t);
    }
}

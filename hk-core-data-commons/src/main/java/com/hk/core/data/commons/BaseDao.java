package com.hk.core.data.commons;

import com.google.common.collect.Lists;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: huangkai
 * @date 2018-06-07 14:31
 */
@NoRepositoryBean
public interface BaseDao<T extends Persistable<ID>, ID extends Serializable> extends Insert<T, ID>, Delete<T, ID>, Update<T, ID>, Select<T, ID> {

    default T insertOrUpdate(T t) {
        return t.isNew() ? insert(t) : updateById(t);
    }

    /**
     * 批量保存或更新
     *
     * @param entities
     * @return
     */
    default Iterable<T> insertOrUpdate(Iterable<T> entities) {
        List<T> result = Lists.newArrayList();
        for (T entity : entities) {
            result.add(this.insertOrUpdate(entity));
        }
        return result;
    }
}

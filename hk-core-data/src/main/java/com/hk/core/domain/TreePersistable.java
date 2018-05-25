package com.hk.core.domain;

import org.springframework.data.domain.Persistable;

import java.util.Set;

/**
 * 树形结构实体
 *
 * @author: huangkai
 * @date 2018-05-11 17:21
 */
public interface TreePersistable<T> extends Persistable<String> {

    /**
     * @return
     */
    T getParent();

    /**
     * @param parent
     */
    void setParent(T parent);

    /**
     * @return
     */
    Set<T> getChild();

    /**
     * @param child
     */
    void setChild(Set<T> child);
}

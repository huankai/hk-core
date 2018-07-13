package com.hk.core.data.commons.domain;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Set;

/**
 * Tree Persistable
 *
 * @author: kevin
 * @date 2018-06-07 12:18
 */
@Deprecated
public interface TreePersistable<T, ID extends Serializable> extends Persistable<ID> {

    /**
     * @param parent
     */
    void setParent(T parent);

    /**
     * @return
     */
    T getParent();

    /**
     * @return
     */
    Set<T> getChilds();

    void setChilds(Set<T> childs);
}

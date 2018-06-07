package com.hk.core.data.commons.domain;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;

/**
 * Tree Persistable
 *
 * @author: huangkai
 * @date 2018-06-07 12:18
 */
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
    Collection<T> getChilds();
}

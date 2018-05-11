package com.hk.core.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * @author: huangkai
 * @date 2018-05-11 17:17
 */
@MappedSuperclass
public abstract class AbstractTreePersistable<T> extends AbstractAuditable implements TreePersistable<T> {

    /**
     * 上级
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private T parent;

    /**
     * 子级
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Set<T> child;


    @Override
    public final T getParent() {
        return parent;
    }

    @Override
    public final void setParent(T parent) {
        this.parent = parent;
    }

    @Override
    public final Set<T> getChild() {
        return child;
    }

    @Override
    public final void setChild(Set<T> child) {
        this.child = child;
    }
}

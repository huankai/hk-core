package com.hk.core.domain;

import java.util.Set;

/**
 * 树形结构，会级联删除，慎用.
 *
 * @author: kevin
 * @date 2018-05-11 17:17
 */
public abstract class AbstractTreePersistable<T> extends AbstractAuditable implements TreePersistable<T> {

    /**
     * 上级
     */
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "parent_id", referencedColumnName = "id",updatable = false)
    private T parent;

    /**
     * 子级
     */
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.REMOVE)
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

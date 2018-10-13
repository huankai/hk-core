package com.hk.core.data.jdbc.domain;

import com.hk.commons.util.StringUtils;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.util.ClassUtils;

import java.io.Serializable;

/**
 * @author: sjq-278
 * @date: 2018-10-10 16:32
 */
public abstract class AbstractUUIDPersistable implements Persistable<String>, Serializable {

    @Id
    @Setter
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return StringUtils.isEmpty(id);
    }

    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(ClassUtils.getUserClass(obj))) {
            return false;
        }
        AbstractUUIDPersistable that = (AbstractUUIDPersistable) obj;
        return null != this.getId() && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }
}
package com.hk.core.data.jdbc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.data.jdbc.annotations.NonUpdate;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.util.ClassUtils;

import java.io.Serializable;

/**
 * @author: kevin
 * @date: 2018-10-10 16:32
 */
public abstract class AbstractUUIDPersistable implements Persistable<String>, Serializable {

    @Id
    @Setter
    @NonUpdate
    private String id;

    @Transient
    @JsonIgnore
    private transient boolean isNew = false;

    @Override
    public String getId() {
        return id;
    }

    @Override
    @JsonIgnore
    @Transient
    public boolean isNew() {
        return isNew || StringUtils.isEmpty(id);
    }

    public final void generateId(String id) {
        AssertUtils.state(!isNew(), "id已存在,this id :" + this.id + ",args id:" + id);
        isNew = true;
        this.id = id;
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

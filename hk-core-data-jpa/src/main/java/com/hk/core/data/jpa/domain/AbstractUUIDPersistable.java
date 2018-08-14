package com.hk.core.data.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hk.commons.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.util.ClassUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 基于 UUID的主键生成
 *
 * @author: kevin
 * @date 2017年12月11日下午8:30:33
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class AbstractUUIDPersistable implements Persistable<String>,Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Getter
    @Setter
    private String id;

    @Transient // DATAJPA-622
    @JsonIgnore
    public final boolean isNew() {
        return StringUtils.isEmpty(getId());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }

}

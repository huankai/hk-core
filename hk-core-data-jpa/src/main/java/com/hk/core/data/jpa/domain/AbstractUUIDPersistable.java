package com.hk.core.data.jpa.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hk.commons.util.JsonUtils;
import com.hk.commons.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.util.ClassUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * 基于 UUID的主键生成
 * </p>
 * <p>
 * JsonFilter : 过滤某一些字段
 * </p>
 *
 * @author kevin
 * @date 2017年12月11日下午8:30:33
 */
@MappedSuperclass
@JsonFilter(value = JsonUtils.IGNORE_ENTITY_SERIALIZE_FIELD_FILTER_ID)
@SuppressWarnings("serial")
public abstract class AbstractUUIDPersistable implements Persistable<String>, Serializable {

    /**
     * id 生成策略,strategy 的值可以为 {@link org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory} 中注册的key，也可以为 class 的全类名
     *
     * @see org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Getter
    @Setter
    @Column(name = "id", updatable = false)
    private String id;

    @Transient // DATAJPA-622
    @JsonIgnore
    public final boolean isNew() {
        return StringUtils.isEmpty(getId());
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

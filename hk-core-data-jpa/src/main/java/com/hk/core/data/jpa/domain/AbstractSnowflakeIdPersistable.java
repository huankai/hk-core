package com.hk.core.data.jpa.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hk.commons.util.JsonUtils;
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
 * 使用雪花id 算法生成id
 *
 * @author kevin
 * @date 2019-7-2 16:57
 */
@MappedSuperclass
@JsonFilter(value = JsonUtils.IGNORE_ENTITY_SERIALIZE_FIELD_FILTER_ID)
public abstract class AbstractSnowflakeIdPersistable implements Persistable<Long>, Serializable {


    /**
     * id 生成策略,strategy 的值可以为 {@link org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory} 中注册的key，也可以为 class 的全类名
     *
     * @see org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory
     */
    @Id
    @GeneratedValue(generator = "system-snowflake-id")
    @GenericGenerator(name = "system-snowflake-id", strategy = "org.hibernate.id.SnowflakeIdentifierGenerator")
    @Getter
    @Setter
    private Long id;

    @Transient // DATAJPA-622
    @JsonIgnore
    public final boolean isNew() {
        return null == id;
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
        AbstractSnowflakeIdPersistable that = (AbstractSnowflakeIdPersistable) obj;
        return null != this.getId() && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }
}

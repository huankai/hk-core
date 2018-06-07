package com.hk.core.domain;

import com.hk.commons.util.StringUtils;
import org.springframework.data.domain.Persistable;

/**
 * 基于 UUID的主键生成
 *
 * @author huangkai
 * @date 2017年12月11日下午8:30:33
 */
//@MappedSuperclass
public abstract class AbstractUUIDPersistable implements Persistable<String> {

//    @Id
//    @GeneratedValue(generator = "system-uuid")
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Override
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

//    @Transient // DATAJPA-622
//    @JSONField(serialize = false, deserialize = false)
    public boolean isNew() {
        return StringUtils.isEmpty(getId());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
//    @Override
//    public String toString() {
//        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
//    }
//
//    /*
//     * (non-Javadoc)
//     *
//     * @see java.lang.Object#equals(java.lang.Object)
//     */
//    @Override
//    public boolean equals(Object obj) {
//        if (null == obj) {
//            return false;
//        }
//        if (this == obj) {
//            return true;
//        }
//        if (!getClass().equals(ClassUtils.getUserClass(obj))) {
//            return false;
//        }
//        AbstractPersistable<?> that = (AbstractPersistable<?>) obj;
//        return null != this.getId() && this.getId().equals(that.getId());
//    }
//
//    /*
//     * (non-Javadoc)
//     *
//     * @see java.lang.Object#hashCode()
//     */
//    @Override
//    public int hashCode() {
//        int hashCode = 17;
//        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
//        return hashCode;
//    }

}

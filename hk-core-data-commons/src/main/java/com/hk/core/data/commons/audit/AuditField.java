package com.hk.core.data.commons.audit;

/**
 * audit 字段
 *
 * @author huangkai
 * @date 2018-12-20 11:30
 * @see org.springframework.data.domain.Auditable
 */
public interface AuditField {

    /**
     * 记录创建者
     */
    String CREATED_BY = "created_by";

    /**
     * 记录创建时间
     */
    String CREATED_DATE = "created_date";

    /**
     * 记录最后更新者
     */
    String LAST_MODIFIED_BY = "last_modified_by";

    /**
     * 记录最后更新时间
     */
    String LAST_MODIFIED_DATE = "last_modified_date";
}

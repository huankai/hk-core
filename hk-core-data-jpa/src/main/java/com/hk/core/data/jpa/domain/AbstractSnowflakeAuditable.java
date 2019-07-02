package com.hk.core.data.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hk.core.data.commons.audit.AuditField;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author kevin
 * @date 2019-7-2 16:59
 */
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@JsonIgnoreProperties(value = {"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
public class AbstractSnowflakeAuditable extends AbstractSnowflakeIdPersistable implements Auditable<Long, Long, LocalDateTime>, AuditField {

    /**
     * <pre>
     * 创建记录的用户
     * 此字段和 created_date 都只需要在 insert 的时候才记录，每次修改的时候，不需要update 这两个字段
     * 如果 updatable 设置为true,使用 jpa的 auditing 功能，在修改记录的时候，并不会设置这两个值
     *
     * @see AuditingEntityListener#touchForCreate(Object)
     * @see org.springframework.data.auditing.AuditingHandler#markCreated(Object)
     *
     * @see AuditingEntityListener#touchForUpdate(Object)
     * @see org.springframework.data.auditing.AuditingHandler#markModified(Object)
     * </pre>
     */
    @CreatedBy
    @Column(name = CREATED_BY, updatable = false)
    private Long createdBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = CREATED_DATE, updatable = false)
    private LocalDateTime createdDate;

    /**
     * 最后更新用户
     */
    @Column(name = LAST_MODIFIED_BY)
    @LastModifiedBy
    private Long lastModifiedBy;

    /**
     * 最后更新时间
     */
    @LastModifiedDate
    @Column(name = LAST_MODIFIED_DATE)
    private LocalDateTime lastModifiedDate;

    @Override
    public Optional<Long> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    @Override
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Optional<LocalDateTime> getCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    @Override
    public void setCreatedDate(LocalDateTime creationDate) {
        this.createdDate = creationDate;
    }

    @Override
    public Optional<Long> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Optional<LocalDateTime> getLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    @Override
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}

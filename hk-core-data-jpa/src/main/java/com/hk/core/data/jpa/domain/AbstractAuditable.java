package com.hk.core.data.jpa.domain;

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
 * <pre>
 * 此类实现 jpa 的审核功能
 * 所有继承于此类的实体都要有以下几个字段
 * created_by 创建记录人，新增时会保存
 * created_date 创建时间，新增时会保存
 * last_modified_by  更新记录人，新增时会保存,每次更新时会修改
 * last_modified_date 更新时间，新增时会保存,每次更新时会修改
 * </pre>
 *
 * @author: kevin
 */
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class AbstractAuditable extends AbstractUUIDPersistable implements Auditable<String, String, LocalDateTime> {

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
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    /**
     * 最后更新用户
     */
    @Column(name = "last_modified_by")
    @LastModifiedBy
    private String lastModifiedBy;

    /**
     * 最后更新时间
     */
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Override
    public Optional<String> getCreatedBy() {
        return Optional.of(createdBy);
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Optional<LocalDateTime> getCreatedDate() {
        return Optional.of(createdDate);
    }

    @Override
    public void setCreatedDate(LocalDateTime creationDate) {
        this.createdDate = creationDate;
    }

    @Override
    public Optional<String> getLastModifiedBy() {
        return Optional.of(lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Optional<LocalDateTime> getLastModifiedDate() {
        return Optional.of(lastModifiedDate);
    }

    @Override
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}

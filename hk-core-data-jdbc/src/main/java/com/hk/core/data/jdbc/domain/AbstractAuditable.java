package com.hk.core.data.jdbc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hk.core.data.jdbc.annotations.NonUpdate;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Auditable;
import org.springframework.data.relational.core.mapping.Column;

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
@SuppressWarnings("serial")
public abstract class AbstractAuditable extends AbstractUUIDPersistable implements Auditable<String, String, LocalDateTime> {

    /**
     * <pre>
     * 创建记录的用户
     * 此字段和 created_date 都只需要在 insert 的时候才记录，每次修改的时候，不需要update 这两个字段
     * 如果 updatable 设置为true,使用 jpa的 auditing 功能，在修改记录的时候，并不会设置这两个值
     *
     * @see org.springframework.data.auditing.AuditingHandler#markCreated(Object)
     *
     * @see org.springframework.data.auditing.AuditingHandler#markModified(Object)
     * </pre>
     */
    @CreatedBy
    @JsonIgnore
    @Column(value = "created_by")
    @NonUpdate
    private String createdBy;


    /**
     * 创建时间
     */
    @CreatedDate
    @JsonIgnore
    @Column(value = "created_date")
    @NonUpdate
    private LocalDateTime createdDate;

    /**
     * 最后更新用户
     */
    @Column(value = "last_modified_by")
    @JsonIgnore
    @LastModifiedBy
    private String lastModifiedBy;

    /**
     * 最后更新时间
     */
    @LastModifiedDate
    @JsonIgnore
    @Column(value = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Override
    public Optional<String> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public void setCreatedBy(Optional<String> createByOpt) {
        this.createdBy = createByOpt.orElse(null);
    }

    @Override
    public void setCreatedBy(String createdBy) {
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

    public void setCreatedDate(Optional<LocalDateTime> creationDateOpt) {
        this.createdDate = creationDateOpt.orElse(null);
    }

    @Override
    public Optional<String> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public void setLastModifiedBy(Optional<String> lastModifiedByOpt) {
        this.lastModifiedBy = lastModifiedByOpt.orElse(null);
    }

    @Override
    public Optional<LocalDateTime> getLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    @Override
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setLastModifiedDate(Optional<LocalDateTime> lastModifiedDateOpt) {
        this.lastModifiedDate = lastModifiedDateOpt.orElse(null);
    }
}

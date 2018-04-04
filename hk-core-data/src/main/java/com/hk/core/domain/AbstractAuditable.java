package com.hk.core.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.alibaba.fastjson.annotation.JSONField;

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
 * @author huangkai
 *
 */
@MappedSuperclass
@SuppressWarnings("serial")
@EntityListeners(value = { AuditingEntityListener.class })
public class AbstractAuditable extends AbstractUUIDPersistable implements Auditable<String, String> {

	@CreatedBy
	@Column(name = "created_by")
	@JSONField(deserialize = false, serialize = false)
	private String createdBy;

	@Column(name = "created_date")
	@CreatedDate
	private LocalDateTime createdDate;

	@Column(name = "last_modified_by")
	@LastModifiedBy
	@JSONField(deserialize = false, serialize = false)
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;

	@Override
	public String getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	// @JSONField(deserialize = false, serialize = false)
	public DateTime getCreatedDate() {
		return null == createdDate ? null
				: new DateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
	}

	@Override
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = null == createdDate ? null
				: LocalDateTime.ofInstant(createdDate.toDate().toInstant(), ZoneId.systemDefault());
	}

	@Override
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	@Override
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Override
	@JSONField(deserialize = false, serialize = false)
	public DateTime getLastModifiedDate() {
		return null == lastModifiedDate ? null
				: new DateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
	}

	@Override
	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = null == lastModifiedDate ? null
				: LocalDateTime.ofInstant(lastModifiedDate.toDate().toInstant(), ZoneId.systemDefault());
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}

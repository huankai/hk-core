package com.hk.core.data.jpa.repository;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * 使用 Long 做为主键类型
 * @param <T>
 */
@NoRepositoryBean
public interface LongIdJpaRepository<T extends Persistable<Long>> extends BaseJpaRepository<T, Long> {

}

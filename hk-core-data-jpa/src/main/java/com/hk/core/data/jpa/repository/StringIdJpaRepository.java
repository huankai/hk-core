package com.hk.core.data.jpa.repository;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * 使用String 做为主键类型
 *
 * @param <T>
 */
@NoRepositoryBean
public interface StringIdJpaRepository<T extends Persistable<String>> extends BaseJpaRepository<T, String> {

}

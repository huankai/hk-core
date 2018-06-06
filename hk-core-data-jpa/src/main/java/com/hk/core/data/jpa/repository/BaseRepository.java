package com.hk.core.data.jpa.repository;

import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;


/**
 * @param <T>
 * @param <PK>
 * @author huangkai
 */
@NoRepositoryBean
public interface BaseRepository<T extends Persistable<PK>, PK extends Serializable> extends JpaRepository<T, PK> /* ,JpaSpecificationExecutor<T>*/ {

}

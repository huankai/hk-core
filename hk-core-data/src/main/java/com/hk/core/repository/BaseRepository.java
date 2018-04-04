package com.hk.core.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * 
 * @author huangkai
 *
 * @param <T>
 * @param <PK>
 */
@NoRepositoryBean
public interface BaseRepository<T,PK extends Serializable> extends JpaRepository<T,PK> /*,JpaSpecificationExecutor<T>*/ {

}

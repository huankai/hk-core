package com.hk.core.data.jpa.repository;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface StringRepository<T extends Persistable<String>> extends BaseRepository<T, String> {

}

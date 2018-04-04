package com.hk.core.repository;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface StringRepository<T> extends BaseRepository<T, String> {

}

package com.hk.core.repository;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface LongRepository<T> extends BaseRepository<T, Long> {

}

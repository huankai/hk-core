package com.hk.core.data.jpa.repository;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface LongRepository<T extends Persistable<Long>> extends BaseRepository<T, Long> {

}

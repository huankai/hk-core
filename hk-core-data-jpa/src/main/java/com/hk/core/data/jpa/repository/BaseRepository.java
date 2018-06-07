package com.hk.core.data.jpa.repository;

import com.hk.core.data.commons.BaseDao;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;


/**
 * @param <T>
 * @param <ID>
 * @author huangkai
 */
@NoRepositoryBean
public interface BaseRepository<T extends Persistable<ID>, ID extends Serializable> extends BaseDao<T, ID>, JpaRepository<T, ID> {

    default ExampleMatcher ofExampleMatcher() {
        return ExampleMatcher.matching().withIgnoreNullValues();
    }

}

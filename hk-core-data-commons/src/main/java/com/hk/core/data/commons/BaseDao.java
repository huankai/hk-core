package com.hk.core.data.commons;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @author: kevin
 * @date 2018-06-07 14:31
 */
@NoRepositoryBean
public interface BaseDao<T extends Persistable<ID>, ID extends Serializable> extends Insert<T, ID>, Delete<T, ID>, Update<T, ID>, Select<T, ID> {

}

package com.hk.core.service.jdbc;

import com.hk.core.service.BaseService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * @author kevin
 * @date 2018-10-11 14:45
 */
public interface JdbcBaseService<T extends Persistable<ID>, ID extends Serializable> extends BaseService<T, ID>, JdbcSelectService<T, ID>, JdbcDeleteService<T, ID> {

}

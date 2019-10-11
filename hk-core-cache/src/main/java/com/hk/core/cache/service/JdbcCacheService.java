package com.hk.core.cache.service;


import com.hk.core.service.jdbc.JdbcBaseService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * @author huangkai
 * @date 2019-01-22 11:02
 */
public interface JdbcCacheService<T extends Persistable<ID>, ID extends Serializable> extends JdbcBaseService<T, ID>, CacheService {

}

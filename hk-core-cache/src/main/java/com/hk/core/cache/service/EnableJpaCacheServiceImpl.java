package com.hk.core.cache.service;

import com.hk.core.service.jpa.impl.JpaServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * //TODO 未实现 Service implementation Enable Cache.
 *
 * @author: kevin
 * @date: 2018-05-16 09:58
 * @see com.hk.core.cache.spring.FixUseSupperClassAnnotationParser
 * @see com.hk.core.cache.spring.FixUseSupperClassCacheOperationSource
 */
public abstract class EnableJpaCacheServiceImpl<T extends Persistable<PK>, PK extends Serializable> extends JpaServiceImpl<T, PK> {

    protected final EnableJpaCacheServiceImpl<T, PK> getCurrentProxy() {
        return (EnableJpaCacheServiceImpl<T, PK>) AopContext.currentProxy();
    }

}

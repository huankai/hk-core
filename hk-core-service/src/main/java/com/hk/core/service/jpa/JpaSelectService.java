package com.hk.core.service.jpa;

import com.hk.core.service.SelectService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author kevin
 * @date 2018-10-10 10:32
 */
public interface JpaSelectService<T extends Persistable<ID>, ID extends Serializable> extends SelectService<T, ID> {

    /**
     * @param id id
     * @return T
     */
    T getOne(ID id);

    /**
     * @param t t
     * @return T
     */
    Optional<T> findOne(T t);
}

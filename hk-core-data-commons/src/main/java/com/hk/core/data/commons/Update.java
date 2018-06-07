package com.hk.core.data.commons;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * @author huangkai
 * @date 2018-6-6 22:38
 */
public interface Update<T extends Persistable<ID>, ID extends Serializable> {

    /**
     * 根据主键更新
     *
     * @param t
     * @return
     */
    boolean updateByPrimaryKey(T t);

    /**
     * 根据主键只更新不为空的字段
     *
     * @param t
     * @return
     */
    boolean updateByPrimaryKeySelective(T t);


}

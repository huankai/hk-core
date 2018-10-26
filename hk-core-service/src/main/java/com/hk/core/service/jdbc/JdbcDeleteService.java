package com.hk.core.service.jdbc;

import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.service.DeleteService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * @author: sjq-278
 * @date: 2018-10-26 10:25
 */
public interface JdbcDeleteService<T extends Persistable<ID>, ID extends Serializable> extends DeleteService<T, ID> {

    /**
     * 根据条件删除
     *
     * @param conditions conditions
     * @return true or false
     */
    boolean delete(CompositeCondition conditions);
}

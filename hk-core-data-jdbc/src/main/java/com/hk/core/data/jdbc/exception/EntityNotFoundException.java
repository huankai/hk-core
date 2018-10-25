package com.hk.core.data.jdbc.exception;

import org.springframework.dao.DataAccessException;

/**
 * @author: sjq-278
 * @date: 2018-10-23 11:36
 */
public class EntityNotFoundException extends DataAccessException {

    public EntityNotFoundException() {
        super("实体不存在或已删除");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}

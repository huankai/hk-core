package com.hk.core.data.jdbc.exception;

import org.springframework.dao.DataAccessException;

/**
 * 实体查询为空时的异常
 * @author kevin
 * @date 2018-10-23 11:36
 */
@SuppressWarnings("serial")
public class EntityNotFoundException extends DataAccessException {

    public EntityNotFoundException() {
        super("实体不存在或已删除");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}

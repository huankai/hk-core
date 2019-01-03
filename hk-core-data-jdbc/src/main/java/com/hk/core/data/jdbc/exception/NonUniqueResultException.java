package com.hk.core.data.jdbc.exception;

import org.springframework.dao.DataAccessException;

/**
 * @author huangkai
 * @date 2018-12-20 10:06
 */
@SuppressWarnings("serial")
public class NonUniqueResultException extends DataAccessException {

    public NonUniqueResultException(String msg) {
        super(msg);
    }

    public NonUniqueResultException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

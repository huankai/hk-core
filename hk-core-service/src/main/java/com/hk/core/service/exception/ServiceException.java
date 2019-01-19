package com.hk.core.service.exception;

/**
 * Http Status Code ：400
 * @author kevin
 * @date 2018-08-03 09:52
 */
@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable t) {
        super(message, t);
    }
}

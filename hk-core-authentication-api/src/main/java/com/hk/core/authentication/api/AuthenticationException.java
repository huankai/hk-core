package com.hk.core.authentication.api;

/**
 * @author: kevin
 * @date: 2018-07-27 14:28
 */
@SuppressWarnings("serial")
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationException(String msg) {
        super(msg);
    }
}

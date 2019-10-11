package com.hk.core.service.exception;

import com.hk.commons.JsonResult;
import lombok.Getter;

/**
 * Http Status Code ：400
 *
 * @author kevin
 * @date 2018-08-03 09:52
 */
@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {

    @Getter
    private int statusCode = 400;

    @Getter
    private final JsonResult<?> result;

    public ServiceException(String message) {
        super(message);
        this.result = JsonResult.error(message);
    }

    public ServiceException(JsonResult<?> result) {
        super(result.getMessage());
        this.result = result;
    }

    public ServiceException(int statusCode, JsonResult<?> result) {
        super(result.getMessage());
        this.result = result;
        this.statusCode = statusCode;

    }

    public ServiceException(String message, Throwable t) {
        super(message, t);
        this.result = JsonResult.error(message);
    }
}

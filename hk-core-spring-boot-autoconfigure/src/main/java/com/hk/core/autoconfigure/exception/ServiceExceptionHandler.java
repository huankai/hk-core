package com.hk.core.autoconfigure.exception;

import com.hk.commons.JsonResult;
import com.hk.core.service.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huangkai
 * @date 2019-3-26 13:52
 */
@RestControllerAdvice
public class ServiceExceptionHandler extends AbstractExceptionHandler {

    /**
     * 对于 ServiceException
     *
     * @param e ServiceException
     * @return {@link JsonResult}
     */
    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<JsonResult<?>> serviceException(ServiceException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return ResponseEntity.status(e.getStatusCode()).body(e.getResult());
    }
}

package com.hk.core.autoconfigure.exception;

import com.hk.commons.JsonResult;
import com.hk.commons.Status;
import com.hk.commons.util.SpringContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

/**
 * jpa 异常处理器
 *
 * @author huangkai
 * @date 2018-10-22 22:22
 */
@RestControllerAdvice
public class JpaExceptionHandler extends AbstractExceptionHandler {

    /**
     * <p>
     * getOne(String id) 无此记录时，将抛出 EntityNotFoundException.<br/>
     * 响应状态码： 404
     * </p>
     *
     * @param e       e
     * @param request request
     * @return {@link JsonResult}
     */
    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JsonResult<Void> entityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return new JsonResult<>(Status.NOT_FOUND, SpringContextHolder.getMessage("access.resource.not.found.message"));
    }
}

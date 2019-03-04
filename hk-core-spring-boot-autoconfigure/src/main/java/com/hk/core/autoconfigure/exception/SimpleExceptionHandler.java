package com.hk.core.autoconfigure.exception;

import com.hk.commons.util.EnumDisplayUtils;
import com.hk.core.service.exception.ServiceException;
import com.hk.commons.JsonResult;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kevin
 * @date 2018-08-03 10:00
 */
@RestControllerAdvice
public class SimpleExceptionHandler extends AbstractExceptionHandler {

    /**
     * 对于 ServiceException
     *
     * @param e ServiceException
     * @return jsonResult
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResult<?> serviceException(ServiceException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return e.getResult();
    }

    /**
     * 404
     * 需要在 application.yml中配置 spring.mvc.throw-exception-if-no-handler-found: true
     * 默认为false（404时不抛出异常）
     *
     * @param e       e
     * @param request request
     * @return jsonResult
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JsonResult<Void> serviceException(NoHandlerFoundException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return new JsonResult<>(JsonResult.Status.NOT_FOUND,
                String.format("%s %s %s ", EnumDisplayUtils.getDisplayText(JsonResult.Status.NOT_FOUND.name(), JsonResult.Status.class), e.getHttpMethod(), e.getRequestURL()));
    }


    /**
     * <p>
     * 对于不是指定的方法请求时，将抛出 HttpRequestMethodNotSupportedException.<br/>
     * 响应状态码： 405
     * </p>
     *
     * @param e       e
     * @param request request
     * @return jsonResult
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public JsonResult<Void> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return new JsonResult<>(JsonResult.Status.METHOD_NOT_ALLOWED, "您访问的资源不支持此方式 :" + e.getMethod());
    }

    /**
     * <p>
     * 客户端请求参数格式不正确或请求出错时，将抛出 ServletRequestBindingException.<br/>
     * 响应状态码： 400
     * </p>
     *
     * @param e       e
     * @param request request
     * @return jsonResult
     */
    @ExceptionHandler(value = {ServletRequestBindingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult<Void> servletRequestBindingException(ServletRequestBindingException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return JsonResult.badRequest("请求出错");
    }

    /**
     * 对数据库操作出现的所有异常： DataAccessException
     *
     * @param e DataAccessException
     * @return jsonResult
     */
    @ExceptionHandler(value = {DataAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResult<Void> dataAccessException(DataAccessException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return JsonResult.error("服务器开了点小差，请稍后再试！");
    }

    /**
     * JSR验证不通过处理
     *
     * @param e       MethodArgumentNotValidException
     * @param request request
     * @return JsonResult
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult<Void> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError.getField() + fieldError.getDefaultMessage();
        error(e, message, request);
        return JsonResult.badRequest(message);
    }

    /**
     * 服务器异常
     *
     * @param e Throwable
     * @return jsonResult
     */
    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResult<Void> throwable(Throwable e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return JsonResult.error(e.getMessage());
    }

}

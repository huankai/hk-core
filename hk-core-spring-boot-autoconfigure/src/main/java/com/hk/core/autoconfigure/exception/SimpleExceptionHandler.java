package com.hk.core.autoconfigure.exception;

import com.hk.commons.JsonResult;
import com.hk.commons.Status;
import com.hk.commons.util.EnumDisplayUtils;
import com.hk.commons.util.SpringContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

/**
 * @author kevin
 * @date 2018-08-03 10:00
 */
@RestControllerAdvice
public class SimpleExceptionHandler extends AbstractExceptionHandler {

    /**
     * 404
     * 需要在 application.yml中配置 spring.mvc.throw-exception-if-no-handler-found: true
     * 默认为false（404时不抛出异常）
     *
     * @param e       e
     * @param request request
     * @return {@link JsonResult}
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JsonResult<Void> noHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return new JsonResult<>(Status.NOT_FOUND,
                String.format("%s %s %s ", EnumDisplayUtils.getDisplayText(Status.NOT_FOUND.name(), Status.class), e.getHttpMethod(), e.getRequestURL()));
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JsonResult<Void> fileNotFountException(FileNotFoundException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return JsonResult.badRequest(SpringContextHolder.getMessage("file.not.found.message"));
    }

    /**
     * <p>
     * 对于不是指定的方法请求时，将抛出 HttpRequestMethodNotSupportedException.<br/>
     * 响应状态码： 405
     * </p>
     *
     * @param e       e
     * @param request request
     * @return {@link JsonResult}
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public JsonResult<Void> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return new JsonResult<>(Status.METHOD_NOT_ALLOWED, SpringContextHolder.getMessage("access.resources.no.support.message", e.getMethod()));
    }

    /**
     * 当使用 {@link org.springframework.web.bind.annotation.RequestBody} 获取请求体数据时，如果body 为 null，则抛出此异常
     *
     * @param e       e
     * @param request request
     * @return {@link JsonResult}
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult<Void> httpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return JsonResult.badRequest(SpringContextHolder.getMessage("request.body.empty.message"));
    }

    /**
     * <p>
     * 客户端请求参数格式不正确或请求出错时，将抛出 ServletRequestBindingException.<br/>
     * 响应状态码： 400
     * </p>
     *
     * @param e       e
     * @param request request
     * @return {@link JsonResult}
     */
    @ExceptionHandler(value = {ServletRequestBindingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult<Void> servletRequestBindingException(ServletRequestBindingException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return JsonResult.badRequest(SpringContextHolder.getMessage("request.error.message"));
    }

    /**
     * 对数据库操作出现的所有异常： DataAccessException
     *
     * @param e DataAccessException
     * @return {@link JsonResult}
     */
    @ExceptionHandler(value = {DataAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResult<Void> dataAccessException(DataAccessException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return JsonResult.error(SpringContextHolder.getMessage("server.error.message"));
    }

    /**
     * JSR验证不通过处理
     *
     * @param e       MethodArgumentNotValidException
     * @param request request
     * @return {@link JsonResult}
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult<Void> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError.getField() + fieldError.getDefaultMessage();
        error(e, message, request);
        return JsonResult.badRequest(message);
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult<Void> bindException(BindException bindException, HttpServletRequest request) {
        FieldError fieldError = bindException.getBindingResult().getFieldError();
        error(bindException, fieldError.getDefaultMessage(), request);
        return JsonResult.badRequest(fieldError.getDefaultMessage());
    }

    /**
     * 服务器异常
     *
     * @param e Throwable
     * @return {@link JsonResult}
     */
    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResult<Void> throwable(Throwable e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return JsonResult.error(e.getMessage());
    }

}

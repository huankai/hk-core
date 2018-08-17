package com.hk.core.autoconfigure.exception;

import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.EnumDisplayUtils;
import com.hk.core.authentication.api.SecurityContextUtils;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.exception.ServiceException;
import com.hk.core.web.JsonResult;
import com.hk.core.web.Webs;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author: kevin
 * @date 2018-08-03 10:00
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //    @Override
//    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleExceptionInternal(ex, body, headers, status, request);
//    }

    public GlobalExceptionHandler() {
        logger.info("GlobalExceptionHandler init...");
    }

    /**
     * 对于 ServiceException
     *
     * @param e ServiceException
     * @return jsonResult
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult serviceException(ServiceException e, HttpServletRequest request) {
        log(e, request);
        return JsonResult.badRueqest(e.getMessage());
    }

    /**
     * 404
     * 需要在 application.yml中配置 spring.mvc.throw-exception-if-no-handler-found: true
     * 默认为false（404时不抛出异常）
     *
     * @param e e
     * @param request request
     * @return jsonResult
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JsonResult serviceException(NoHandlerFoundException e, HttpServletRequest request) {
        log(e, request);
        return new JsonResult(JsonResult.Status.NOT_FOUND,
                String.format("%s %s %s ", EnumDisplayUtils.getDisplayText(JsonResult.Status.NOT_FOUND.name(), JsonResult.Status.class), e.getHttpMethod(), e.getRequestURL()));
    }

    /**
     * <p>
     * getOne(String id) 无此记录时，将抛出 EntityNotFoundException.<br/>
     * 响应状态码： 404
     * </p>
     *
     * @param e e
     * @param request request
     * @return jsonResult
     */
    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JsonResult serviceException(EntityNotFoundException e, HttpServletRequest request) {
        log(e, request);
        return new JsonResult(JsonResult.Status.NOT_FOUND, "您访问的资源可能不存在!");
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
    public JsonResult serviceException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log(e, request);
        return new JsonResult(JsonResult.Status.METHOD_NOT_ALLOWED, "您访问的资源不支持此方式 :" + e.getMethod());
    }

    /**
     * 对数据库操作出现的所有异常： DataAccessException
     *
     * @param e DataAccessException
     * @return jsonResult
     */
    @ExceptionHandler(value = {DataAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResult serviceException(DataAccessException e, HttpServletRequest request) {
        log(e, request);
        return JsonResult.error("服务器开了点小差，请稍后再试！");
    }

    /**
     * 无权限访问
     *
     * @param e       e
     * @param request request
     * @return jsonResult
     */
    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public JsonResult accessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log(e, request);
        return JsonResult.error("您无权限访问，请与管理员联系！");
    }

    /**
     * 服务器异常
     *
     * @param e Throwable
     * @return jsonResult
     */
    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResult throwable(Throwable e, HttpServletRequest request) {
        log(e, request);
        return JsonResult.error(e.getMessage());
    }

    private void log(Throwable e, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.LF);

        sb.append("<------------------------->");
        sb.append(StringUtils.LF);
        if (SecurityContextUtils.isAuthenticated()) {
            UserPrincipal principal = SecurityContextUtils.getPrincipal();
            sb.append("<User id:").append(principal.getUserId()).append(">");
            sb.append(StringUtils.LF);

            sb.append("<User Account:").append(principal.getAccount()).append(">");
            sb.append(StringUtils.LF);
        }

        sb.append("<Request Time:").append(LocalDateTime.now()).append(">");
        sb.append(StringUtils.LF);

        sb.append("<Remote Address:").append(Webs.getRemoteAddr(request)).append(">");
        sb.append(StringUtils.LF);

        sb.append("<Request URI:").append(request.getRequestURI()).append(">");
        sb.append(StringUtils.LF);

        sb.append("<Request Method:").append(request.getMethod()).append(">");
        sb.append(StringUtils.LF);

        Map<String, String[]> parameterMap = request.getParameterMap();
        if (CollectionUtils.isNotEmpty(parameterMap)) {
            sb.append("<Request Params:");
            parameterMap.forEach((name, value) -> sb.append(name).append("=").append(ArrayUtils.toString(value)));
            sb.append(">");
            sb.append(StringUtils.LF);
        }

        sb.append("<ERROR Message:[").append(e.getClass()).append("]:").append(e.getMessage()).append(">");
        sb.append(StringUtils.LF);

        sb.append("<------------------------->");
        logger.error(sb.toString());
    }
}

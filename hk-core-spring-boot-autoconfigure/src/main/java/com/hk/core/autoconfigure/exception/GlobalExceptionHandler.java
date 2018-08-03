package com.hk.core.autoconfigure.exception;

import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.EnumDisplayUtils;
import com.hk.commons.util.JsonUtils;
import com.hk.core.authentication.api.SecurityContext;
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
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author: kevin
 * @date 2018-08-03 10:00
 */
public class GlobalExceptionHandler {


    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private SecurityContext securityContext;


    public GlobalExceptionHandler(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }
    //    @Override
//    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleExceptionInternal(ex, body, headers, status, request);
//    }

    /**
     * 对于 ServiceException
     *
     * @param e ServiceException
     * @return
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String serviceException(ServiceException e, HttpServletRequest request) {
        log(e, request);
        return JsonUtils.serialize(JsonResult.badRueqest(e.getMessage()));
    }

    /**
     * 404
     * 需要在 application.yml中配置 spring.mvc.throw-exception-if-no-handler-found: true
     * 默认为false（404时不抛出异常）
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String serviceException(NoHandlerFoundException e, HttpServletRequest request) {
        log(e, request);
        return JsonUtils.serialize(new JsonResult(JsonResult.Status.NOT_FOUND,
                String.format("%s %s %s ", EnumDisplayUtils.getDisplayText(JsonResult.Status.NOT_FOUND.name(), JsonResult.Status.class), e.getHttpMethod(), e.getRequestURL())));
    }

    /**
     * <p>
     * getOne(String id) 无此记录时，将抛出 EntityNotFoundException.<br/>
     * 响应状态码： 404
     * </p>
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String serviceException(EntityNotFoundException e, HttpServletRequest request) {
        log(e, request);
        return JsonUtils.serialize(new JsonResult(JsonResult.Status.NOT_FOUND, e.getMessage()));
    }

    /**
     * <p>
     * 对于不是指定的方法请求时，将抛出 HttpRequestMethodNotSupportedException.<br/>
     * 响应状态码： 405
     * </p>
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String serviceException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log(e, request);
        return JsonUtils.serialize(new JsonResult(JsonResult.Status.METHOD_NOT_ALLOWED, e.getMessage()));
    }

    /**
     * 对数据库操作出现的所有异常： DataAccessException
     *
     * @param e DataAccessException
     * @return
     */
    @ExceptionHandler(value = {DataAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String serviceException(DataAccessException e, HttpServletRequest request) {
        log(e, request);
        return JsonUtils.serialize(JsonResult.error(e.getMessage()));
    }

    /**
     * 服务器异常
     *
     * @param e Throwable
     * @return
     */
    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String throwable(Throwable e, HttpServletRequest request) {
        log(e, request);
        return JsonUtils.serialize(JsonResult.error(e.getMessage()));
    }

    private void log(Throwable e, HttpServletRequest request) {
        UserPrincipal principal = securityContext.getPrincipal();
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.LF);

        sb.append("<------------------------->");
        sb.append(StringUtils.LF);

        sb.append("<User id:").append(principal.getUserId()).append(">");
        sb.append(StringUtils.LF);

        sb.append("<User Account:").append(principal.getAccount()).append(">");
        sb.append(StringUtils.LF);

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
        e.printStackTrace();
    }
}

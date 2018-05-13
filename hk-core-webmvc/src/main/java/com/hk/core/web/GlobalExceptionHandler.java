package com.hk.core.web;

import com.hk.commons.fastjson.JsonUtils;
import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 全局异常处理器
 *
 * @author: huangkai
 * @date 2018-05-11 15:32
 */
@RestControllerAdvice
public class GlobalExceptionHandler /*extends ResponseEntityExceptionHandler*/ {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private SecurityContext securityContext;

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
        return JsonUtils.toJSONString(JsonResult.badRueqest(e.getMessage()));
    }

    /**
     * 对于 DataAccessException
     *
     * @param e DataAccessException
     * @return
     */
    @ExceptionHandler(value = {DataAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String serviceException(DataAccessException e, HttpServletRequest request) {
        log(e, request);
        return JsonUtils.toJSONString(JsonResult.error(e.getMessage()));
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
        return JsonUtils.toJSONString(JsonResult.error(e.getMessage()));
    }

    private void log(Throwable e, HttpServletRequest request) {
        UserPrincipal principal = securityContext.getPrincipal();
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.LF);

        sb.append("<------------------------->");
        sb.append(StringUtils.LF);

        sb.append("<User id:").append(principal.getUserId()).append(">");
        sb.append(StringUtils.LF);

        sb.append("<User Name:").append(principal.getUserName()).append(">");
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
    }
}

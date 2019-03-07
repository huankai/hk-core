package com.hk.core.autoconfigure.exception;

import com.hk.commons.JsonResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * spring security 异常处理器
 *
 * @author kevin
 * @date 2018-09-10 14:17
 */
@RestControllerAdvice
public class SpringSecurityExceptionHandler extends AbstractExceptionHandler {

    /**
     * 无权限访问
     *
     * @param e       e
     * @param request request
     * @return jsonResult
     */
    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public JsonResult<Void> accessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        error(e, e.getMessage(), request);
        return JsonResult.forbidden("您无权限访问，请与管理员联系！");
    }
}

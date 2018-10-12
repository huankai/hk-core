package com.hk.core.autoconfigure.exception;

import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.api.SecurityContextUtils;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.web.Webs;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author: kevin
 * @date: 2018-09-10 14:14
 */
abstract class AbstractExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected void error(Throwable e, String message, HttpServletRequest request) {
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

        sb.append("<ERROR:[").append(e.getClass()).append("]:").append(e).append(">");
        sb.append("<ERROR Message:[").append(e.getClass()).append("]:").append(StringUtils.isEmpty(message) ? e.getMessage() : message).append(">");
        sb.append(StringUtils.LF);

        sb.append("<------------------------->");
        logger.error(sb.toString());
        e.printStackTrace();
    }
}

package com.hk.core.authentication.security.accesstoken;


import com.hk.commons.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huangkai
 * @date 2019/3/5 17:03
 */
abstract class TokenUtils {

    static String getAccessToken(HttpServletRequest request, String header, String tokenParameter) {
        String token = request.getHeader(header);
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(tokenParameter);
        }
        return token;
    }

}

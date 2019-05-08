package com.hk.core.web.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author huangkai
 * @date 2019-05-08 22:57
 */
public interface CookieValueManager {

    String buildCookieValue(String givenCookieValue, HttpServletRequest request);

    /**
     * 获取 Cookie 值.
     *
     * @param cookie  the cookie
     * @param request the request
     * @return the cookie value or null
     */
    String obtainCookieValue(Cookie cookie, HttpServletRequest request);


    CookieValueManager NO_OP_COOKIE_MANAGER = new CookieValueManager() {
        @Override
        public String buildCookieValue(String givenCookieValue, HttpServletRequest request) {
            return givenCookieValue;
        }

        @Override
        public String obtainCookieValue(Cookie cookie, HttpServletRequest request) {
            return cookie.getValue();
        }
    };
}

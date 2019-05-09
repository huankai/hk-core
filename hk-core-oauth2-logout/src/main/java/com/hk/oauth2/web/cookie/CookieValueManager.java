package com.hk.oauth2.web.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author huangkai
 * @date 2019-05-08 22:57
 */
public interface CookieValueManager {

    /**
     * 构建 Cookie 值
     *
     * @param givenCookieValue givenCookieValue
     * @param request          request
     * @return 新的 Cookie 值
     */
    String buildCookieValue(String givenCookieValue, HttpServletRequest request);

    /**
     * 获取 Cookie 值.
     *
     * @param cookie  the cookie
     * @param request the request
     * @return the cookie value or null
     */
    String obtainCookieValue(Cookie cookie, HttpServletRequest request);

}

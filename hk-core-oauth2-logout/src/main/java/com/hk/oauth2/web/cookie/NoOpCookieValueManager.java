package com.hk.oauth2.web.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author kevin
 * @date 2019-5-9 13:27
 */
public class NoOpCookieValueManager implements CookieValueManager {

    @Override
    public String buildCookieValue(String givenCookieValue, HttpServletRequest request) {
        return givenCookieValue;
    }

    @Override
    public String obtainCookieValue(Cookie cookie, HttpServletRequest request) {
        return cookie.getValue();
    }
}

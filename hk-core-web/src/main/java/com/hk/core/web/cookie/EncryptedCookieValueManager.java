package com.hk.core.web.cookie;

import com.hk.commons.cipher.CipherExecutor;
import com.hk.commons.util.StringUtils;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author huangkai
 * @date 2019-05-08 22:58
 */
@RequiredArgsConstructor
public class EncryptedCookieValueManager implements CookieValueManager {

    /**
     *
     */
    private final CipherExecutor<Serializable, Serializable> cipherExecutor;

    @Override
    public String buildCookieValue(String givenCookieValue, HttpServletRequest request) {
        String res = buildCompoundCookieValue(givenCookieValue, request);
        return cipherExecutor.encode(res).toString();
    }

    protected String buildCompoundCookieValue(final String cookieValue, final HttpServletRequest request) {
        return cookieValue;
    }

    @Override
    public String obtainCookieValue(Cookie cookie, HttpServletRequest request) {
        String cookieValue = cipherExecutor.decode(cookie.getValue(), new Object[]{}).toString();
        return StringUtils.isEmpty(cookieValue) ? null : obtainValueFromCompoundCookie(cookieValue, request);
    }

    protected String obtainValueFromCompoundCookie(final String cookieValue, final HttpServletRequest request) {
        return cookieValue;
    }
}

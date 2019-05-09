package com.hk.oauth2.web.support;

import com.hk.commons.util.StringUtils;
import com.hk.core.web.Webs;
import com.hk.oauth2.web.cookie.CookieValueManager;
import com.hk.oauth2.web.cookie.NoOpCookieValueManager;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 重写 Cookie,使用 {@link CookieValueManager} 添加 user-agent 和 requestIp
 *
 * @author kevin
 * @date 2019-5-9 9:07
 */
@Slf4j
public class CookieRetrievingCookieGenerator extends CookieGenerator {

    private static final int DEFAULT_REMEMBER_ME_MAX_AGE = 7889231;

    /**
     * 使用记住我功能时，remember Cookie 过期时间 ，默认为 3 个月
     */
    @Setter
    private int rememberMeMaxAge = DEFAULT_REMEMBER_ME_MAX_AGE;

    private final CookieValueManager oauth2CookieValueManager;

    public CookieRetrievingCookieGenerator(final String name, final String path, final int maxAge,
                                           final boolean secure, final String domain, final boolean httpOnly) {
        this(name, path, maxAge, secure, domain, new NoOpCookieValueManager(),
                httpOnly);
    }

    public CookieRetrievingCookieGenerator(final String name, final String path, final int maxAge,
                                           final boolean secure, final String domain, final boolean httpOnly,
                                           final CookieValueManager cookieValueManager) {
        this(name, path, maxAge, secure, domain, cookieValueManager, httpOnly);
    }

    public CookieRetrievingCookieGenerator(final String name, final String path, final int maxAge, final boolean secure,
                                           final String domain, final CookieValueManager oauth2CookieValueManager,
                                           final boolean httpOnly) {
        super.setCookieName(name);
        super.setCookiePath(path);
        this.setCookieDomain(domain);
        super.setCookieMaxAge(maxAge);
        super.setCookieSecure(secure);
        super.setCookieHttpOnly(httpOnly);
        this.oauth2CookieValueManager = oauth2CookieValueManager;
    }

    @Override
    public void addCookie(HttpServletResponse response, String cookieValue) {
        HttpServletRequest request = Webs.getHttpServletRequest();
        final String theCookieValue = oauth2CookieValueManager.buildCookieValue(cookieValue, request);
        if (isRememberMeAuthentication(request)) {
            log.debug("Creating cookie [{}] for remember-me authentication with max-age [{}]", getCookieName(), request);
            final Cookie cookie = createCookie(theCookieValue);
            cookie.setMaxAge(rememberMeMaxAge);
            cookie.setSecure(isCookieSecure());
            cookie.setHttpOnly(isCookieHttpOnly());
            cookie.setComment("Oauth2 Cookie w/ Remember-Me");
            response.addCookie(cookie);
        } else {
            log.debug("Creating cookie [{}]", getCookieName());
            super.addCookie(response, theCookieValue);
        }
    }

    public void addCookie(final HttpServletRequest request, final HttpServletResponse response, final String cookieValue) {
        final String theCookieValue = oauth2CookieValueManager.buildCookieValue(cookieValue, request);
        log.debug("Creating cookie [{}]", getCookieName());
        super.addCookie(response, theCookieValue);
    }

    public String retrieveCookieValue(final HttpServletRequest request) {
        try {
            Cookie cookie = WebUtils.getCookie(request, getCookieName());
            if (cookie == null) {
                final String cookieValue = request.getHeader(getCookieName());
                if (StringUtils.isNotEmpty(cookieValue)) {
                    log.debug("Found cookie [{}] under header name [{}]", cookieValue, getCookieName());
                    cookie = createCookie(cookieValue);
                }
            }
            return cookie == null ? null : oauth2CookieValueManager.obtainCookieValue(cookie, request);
        } catch (final Exception e) {
            log.debug(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void setCookieDomain(final String cookieDomain) {
        super.setCookieDomain(StringUtils.defaultIfEmpty(cookieDomain, null));
    }

    @Override
    protected Cookie createCookie(final String cookieValue) {
        final Cookie c = super.createCookie(cookieValue);
        c.setComment("Oauth2 Cookie");
        return c;
    }

    /**
     * 是否为 remember 认证，为false
     *
     * @param request
     * @return
     */
    private boolean isRememberMeAuthentication(HttpServletRequest request) {
        return false;
    }

//    private static Boolean isRememberMeAuthentication(final RequestContext requestContext) {
//        final HttpServletRequest request = WebUtils.getHttpServletRequestFromExternalWebflowContext(requestContext);
//        final String value = request.getParameter(RememberMeCredential.REQUEST_PARAMETER_REMEMBER_ME);
//        LOGGER.debug("Locating request parameter [{}] with value [{}]", RememberMeCredential.REQUEST_PARAMETER_REMEMBER_ME, value);
//        boolean isRememberMe = StringUtils.isNotBlank(value) && WebUtils.isRememberMeAuthenticationEnabled(requestContext);
//        if (!isRememberMe) {
//            LOGGER.debug("Request does not indicate a remember-me authentication event. Locating authentication object from the request context...");
//            final Authentication auth = WebUtils.getAuthentication(requestContext);
//            if (auth != null) {
//                final Map<String, Object> attributes = auth.getAttributes();
//                LOGGER.debug("Located authentication attributes [{}]", attributes);
//                if (attributes.containsKey(RememberMeCredential.AUTHENTICATION_ATTRIBUTE_REMEMBER_ME)) {
//                    final boolean rememberMeValue = (boolean) attributes.getOrDefault(RememberMeCredential.AUTHENTICATION_ATTRIBUTE_REMEMBER_ME, Boolean.FALSE);
//                    LOGGER.debug("Located remember-me authentication attribute [{}]", rememberMeValue);
//                    isRememberMe = CollectionUtils.wrapSet(rememberMeValue).contains(Boolean.TRUE);
//                }
//            }
//        }
//        LOGGER.debug("Is this request from a remember-me authentication event? [{}]", BooleanUtils.toStringYesNo(isRememberMe));
//        return isRememberMe;
//    }
}

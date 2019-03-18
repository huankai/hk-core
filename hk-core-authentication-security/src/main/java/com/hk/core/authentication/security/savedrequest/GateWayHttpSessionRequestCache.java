package com.hk.core.authentication.security.savedrequest;

import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.StringUtils;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author kevin
 * @date 2018-08-28 10:32
 * @see org.springframework.security.web.savedrequest.HttpSessionRequestCache
 */
public class GateWayHttpSessionRequestCache implements RequestCache {

    private static final String SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Setter
    private PortResolver portResolver = new PortResolverImpl();

    @Setter
    private boolean createSessionAllowed = true;

    @Setter
    private RequestMatcher requestMatcher = AnyRequestMatcher.INSTANCE;

    @Setter
    private String sessionAttrName = SAVED_REQUEST;

    /**
     * 使用GateWay缓存上次请求
     */
    private final String gateWayUrl;

    public GateWayHttpSessionRequestCache(String gateWayUrl) {
        AssertUtils.isTrue(StringUtils.startsWithAny(gateWayUrl, "http", "https"), "gateWayUrl must start with : http or https");
        this.gateWayUrl = gateWayUrl;
    }


    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if (requestMatcher.matches(request)) {
            //使用 gateWay 缓存之前访问的请求信息
            GateWaySavedRequest savedRequest = new GateWaySavedRequest(gateWayUrl, request, portResolver);
            if (createSessionAllowed || request.getSession(false) != null) {
                request.getSession().setAttribute(this.sessionAttrName, savedRequest);
                logger.debug("DefaultSavedRequest added to Session: " + savedRequest);
            }
        } else {
            logger.debug("Request not saved as configured RequestMatcher did not match");
        }
    }

    @Override
    public SavedRequest getRequest(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        return session != null ? (SavedRequest) session.getAttribute(sessionAttrName) : null;
    }

    @Override
    public HttpServletRequest getMatchingRequest(HttpServletRequest request, HttpServletResponse response) {
        GateWaySavedRequest saved = (GateWaySavedRequest) getRequest(request, response);
        if (saved == null) {
            return null;
        }
        if (!saved.doesRequestMatch(request, portResolver)) {
            logger.debug("saved request doesn't match");
            return null;
        }
        removeRequest(request, response);

        return new SavedRequestAwareWrapper(saved, request);
    }

    @Override
    public void removeRequest(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            logger.debug("Removing DefaultSavedRequest from session if present");
            session.removeAttribute(sessionAttrName);
        }
    }
}

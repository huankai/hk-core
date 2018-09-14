package com.hk.core.authentication.security.savedrequest;

import com.hk.commons.util.StringUtils;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: kevin
 * @date: 2018-08-28 10:28
 */
public class GateWaySavedRequest extends DefaultSavedRequest {

    /**
     *
     */
    private final String gateWayUrl;

    public GateWaySavedRequest(String gateWayUrl, HttpServletRequest request, PortResolver portResolver) {
        super(request, portResolver);
        this.gateWayUrl = gateWayUrl;
    }

    @Override
    public String getRedirectUrl() {
        String requestURI = super.getRequestURI();
        if (!StringUtils.startsWith(requestURI, "/")) {
            requestURI = "/" + requestURI;
        }
        return gateWayUrl + requestURI;
    }

    @Override
    public String getRequestURL() {
        return gateWayUrl;
    }
}

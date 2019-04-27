package com.hk.core.authentication.oauth2.filter;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.oauth2.session.HashMapBackedSessionMappingStorage;
import com.hk.core.authentication.oauth2.session.SessionMappingStorage;
import com.hk.core.web.filter.AbstractFilter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author kevin
 * @date 2019-4-27 11:50
 */
@Slf4j
public class SingleSignOutFilter extends AbstractFilter {

    private static final String LOGOUT_REQUEST = "logoutRequest";

    @Setter
    private String processUrl;

    public SingleSignOutFilter(String processUrl) {
        this.processUrl = processUrl;
    }

    private SessionMappingStorage sessionMappingStorage = new HashMapBackedSessionMappingStorage();

    @Override
    protected void doInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (HttpMethod.DELETE.matches(request.getMethod()) && isLogoutRequest(request)
                && StringUtils.equals(request.getRequestURI(), processUrl)) {
            HttpSession session = sessionMappingStorage.removeSessionByMappingId(ServletRequestUtils.getRequiredStringParameter(request, LOGOUT_REQUEST));
            if (session != null) {
                log.debug("Invalidating session: " + session.getId());
                session.invalidate();
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isLogoutRequest(HttpServletRequest request) throws ServletRequestBindingException {
        return StringUtils.isNotEmpty(ServletRequestUtils.getStringParameter(request, LOGOUT_REQUEST));
    }


}

package com.hk.core.web.interceptors;

import com.google.common.collect.Maps;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.web.AppCodeUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: huangkai
 * @date 2018-05-15 12:38
 */
public class SecurityContextInterceptor extends HandlerInterceptorAdapter {

    public SecurityContext securityContext;

    private static Map<String, String> appCodeIdMap = Maps.newConcurrentMap();

    public SecurityContextInterceptor(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String appCode = AppCodeUtils.getAppCode();
        String appId = appCodeIdMap.get(appCode);
        if (appId == null) {
            appId = AppCodeUtils.getCurrentAppId();
            appCodeIdMap.put(appCode, appId);
        }
        if(securityContext.isAuthenticated()){
            UserPrincipal principal = securityContext.getPrincipal();
            if (StringUtils.notEquals(principal.getAppId(), appId)) {
                principal.setAppId(appId);
            }
        }
        return true;
    }
}

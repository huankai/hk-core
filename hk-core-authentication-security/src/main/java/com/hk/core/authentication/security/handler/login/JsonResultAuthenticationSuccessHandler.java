package com.hk.core.authentication.security.handler.login;

import com.hk.commons.JsonResult;
import com.hk.core.web.Webs;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 如果是 ajax 请求、android 请求、苹果手机应用请求，使用内部转发，因为使用重定向是客户端行为，
 * 会导致响应码为 302，可能导致客户端不能获取 Json 格式的数据
 *
 * @author kevin
 * @date 2018-07-26 17:28
 */
public class JsonResultAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Setter
    private RequestCache requestCache = new HttpSessionRequestCache();

    public JsonResultAuthenticationSuccessHandler(String forwardUrl) {
        setDefaultTargetUrl(forwardUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String redirectUrl = getDefaultTargetUrl();
        if (savedRequest != null) {
            String targetUrlParameter = getTargetUrlParameter();
            if (isAlwaysUseDefaultTargetUrl()
                    || (targetUrlParameter != null && StringUtils.hasText(request
                    .getParameter(targetUrlParameter)))) {
                requestCache.removeRequest(request, response);
            }

            clearAuthenticationAttributes(request);
            redirectUrl = savedRequest.getRedirectUrl();
        }
        Webs.writeJson(response, HttpServletResponse.SC_OK, JsonResult.redirect(redirectUrl));

    }
}

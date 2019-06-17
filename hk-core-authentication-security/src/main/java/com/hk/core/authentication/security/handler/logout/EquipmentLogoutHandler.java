package com.hk.core.authentication.security.handler.logout;

import com.hk.commons.JsonResult;
import com.hk.commons.util.StringUtils;
import com.hk.core.web.Webs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 根据请求设备执行返回 json 或 重写向操作
 *
 * @author huangkai
 * @date 2018-9-22 20:42
 */
public class EquipmentLogoutHandler implements LogoutHandler {

    private static final String REDIRECT_URL_PARAM_NAME = "redirect_url";

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentLogoutHandler.class);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private String logoutSuccessUrl;

    public EquipmentLogoutHandler(String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (Webs.isAndroid(request) || Webs.isIPhone(request)) {
            Webs.writeJson(response, HttpServletResponse.SC_OK, JsonResult.success("退出成功"));
        } else {
            String redirectUrl = getRedirectUrl(request);
            if (StringUtils.isNotEmpty(redirectUrl)) {
                try {
                    redirectStrategy.sendRedirect(request, response, redirectUrl);
                } catch (IOException e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.debug("重定向到 {} 失败", redirectUrl);
                    }
                }
            } else {
                Webs.writeJson(response, HttpServletResponse.SC_OK, JsonResult.success("退出成功"));
            }
        }
    }

    protected String getRedirectUrl(HttpServletRequest request) {
        String redirectUrl = request.getParameter(REDIRECT_URL_PARAM_NAME);
        if (StringUtils.isEmpty(redirectUrl)) {
            redirectUrl = logoutSuccessUrl;
        }
        return redirectUrl;
    }
}

package com.hk.core.authentication.security.handler.logout;

import com.hk.commons.util.StringUtils;
import com.hk.core.web.JsonResult;
import com.hk.core.web.Webs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: huangkai
 * @date: 2018-9-22 20:42
 */
public class RedirectLogoutHandler implements LogoutHandler {

    private static final String REDIRECT_URL_PARAM_NAME = "redirect_url";

    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectLogoutHandler.class);

    private String logoutSuccessUrl;

    public RedirectLogoutHandler(String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) { String redirectUrl = request.getParameter(REDIRECT_URL_PARAM_NAME);
        if (StringUtils.isEmpty(redirectUrl)) {
            redirectUrl = logoutSuccessUrl;
        }
        if (StringUtils.isNotEmpty(redirectUrl)) {
            try {
                response.sendRedirect(redirectUrl);
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

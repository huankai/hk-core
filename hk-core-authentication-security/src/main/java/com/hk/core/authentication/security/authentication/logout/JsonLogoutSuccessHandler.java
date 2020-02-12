package com.hk.core.authentication.security.authentication.logout;

import com.hk.commons.JsonResult;
import com.hk.core.web.Webs;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kevin
 * @date 2020-02-12 14:19
 */
public class JsonLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Webs.writeJson(response, HttpServletResponse.SC_OK, JsonResult.success("退出成功"));
    }
}

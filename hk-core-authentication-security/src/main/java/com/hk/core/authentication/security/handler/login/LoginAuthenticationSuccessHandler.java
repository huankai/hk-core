package com.hk.core.authentication.security.handler.login;

import com.hk.core.authentication.security.SecurityUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证成功Handler
 *
 * @author kevin
 * @date 2018-07-26 17:28
 */
public class LoginAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthenticationSuccessHandler.class);

    public LoginAuthenticationSuccessHandler() {
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        SecurityUserPrincipal principal = (SecurityUserPrincipal) authentication.getPrincipal();
        LOGGER.info("{} 登陆成功....", principal.getUsername());
//        switch (responseType) {
//            case JSON:
//                Map<String, Object> result = new HashMap<>();
//                result.put("userId", principal.getUserId());
//                result.put("username", principal.getUsername());
//                result.put("sex", principal.getSex());
//                result.put("realName", principal.getRealName());
//                result.put("iconPath", principal.getIconPath());
//                Webs.writeJson(response, HttpStatus.OK.value(), JsonResult.success(result));
//                break;
//            case REDIRECT:
//            default:
//                super.onAuthenticationSuccess(request, response, authentication);
//                break;
//        }
    }
}

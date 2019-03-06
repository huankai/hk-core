package com.hk.core.authentication.security.accesstoken;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 使用 token 退出登陆处理器，退出成功后，删除 token
 *
 * @author huangkai
 * @date 2019/3/6 16:26
 */
public class AccessTokenLogoutHandler implements LogoutHandler {

    private final AccessTokenContext tokenContext;

    private final String header;

    private final String tokenParameter;

    public AccessTokenLogoutHandler(AccessTokenContext tokenContext, String header, String tokenParameter) {
        this.tokenContext = tokenContext;
        this.header = header;
        this.tokenParameter = tokenParameter;
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = TokenUtils.getAccessToken(request, header, tokenParameter);
        tokenContext.removeAccessToken(token);
    }
}

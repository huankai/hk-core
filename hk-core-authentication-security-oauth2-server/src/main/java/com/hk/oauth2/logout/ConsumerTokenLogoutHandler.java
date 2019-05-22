package com.hk.oauth2.logout;

import com.hk.core.authentication.oauth2.utils.AccessTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 失效 access_token 退出处理器
 *
 * @author kevin
 * @date 2019-5-10 17:47
 */
@RequiredArgsConstructor
public class ConsumerTokenLogoutHandler implements LogoutHandler {

    private final ConsumerTokenServices consumerTokenServices;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        consumerTokenServices.revokeToken(AccessTokenUtils.getAccessToken(request));
    }
}

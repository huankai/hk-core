package com.hk.oauth2.logout;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.oauth2.utils.AccessTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通知客户端执行 退出操作
 *
 * @author kevin
 * @date 2019-5-18 11:38
 */
@Slf4j
@RequiredArgsConstructor
public class SingleLogoutHandler implements LogoutHandler {

    private final SingleLogoutServiceMessageHandler singleLogoutServiceMessageHandler;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        var tokenValue = AccessTokenUtils.getAccessToken(request);
        if (StringUtils.isNotEmpty(tokenValue)) {
            singleLogoutServiceMessageHandler.handle(tokenValue);
        }
    }
}

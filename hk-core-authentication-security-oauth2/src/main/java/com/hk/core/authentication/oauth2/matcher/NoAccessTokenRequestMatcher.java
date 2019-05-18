package com.hk.core.authentication.oauth2.matcher;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.oauth2.utils.AccessTokenUtils;
import lombok.NoArgsConstructor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

import static lombok.AccessLevel.PRIVATE;

/**
 * 非 access_token 请求
 *
 * @author kevin
 * @date 2018-08-27 13:52
 */
@NoArgsConstructor(access = PRIVATE)
public class NoAccessTokenRequestMatcher implements RequestMatcher {

    private static final NoAccessTokenRequestMatcher INSTANCE = new NoAccessTokenRequestMatcher();

    public static NoAccessTokenRequestMatcher getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return StringUtils.isEmpty(AccessTokenUtils.getAccessToken(request));
    }
}

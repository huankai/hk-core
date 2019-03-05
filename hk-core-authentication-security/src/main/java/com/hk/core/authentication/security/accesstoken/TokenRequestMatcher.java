package com.hk.core.authentication.security.accesstoken;

import com.hk.commons.util.StringUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huangkai
 * @date 2019/3/5 16:26
 */
public class TokenRequestMatcher implements RequestMatcher {

    private final String header;

    private final String tokenParameter;

    TokenRequestMatcher(String header, String tokenParameter) {
        this.header = header;
        this.tokenParameter = tokenParameter;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return StringUtils.isNotEmpty(TokenUtils.getAccessToken(request, header, tokenParameter));
    }
}

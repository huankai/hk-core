package com.hk.core.authentication.security.accesstoken;

import com.hk.commons.util.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huangkai
 * @date 2019/3/5 15:06
 */
public class AccessTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String AUTHORIZATION = "Authorization";

    public static final String TOKEN_PARAMETER = "access_token";

    private final String header;

    private final String tokenParameter;

    public AccessTokenAuthenticationFilter() {
        this(AUTHORIZATION, TOKEN_PARAMETER);
    }

    public AccessTokenAuthenticationFilter(String header, String tokenParameter) {
        super(new TokenRequestMatcher(header, tokenParameter));
        this.header = header;
        this.tokenParameter = tokenParameter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String token = TokenUtils.getAccessToken(request, header, tokenParameter);
        if (StringUtils.isEmpty(token)) {
            throw new AuthenticationServiceException("token 认证失败");
        }
        AccessTokenAuthenticationToken authenticationToken = new AccessTokenAuthenticationToken(token);
        setDetails(request, authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    private void setDetails(HttpServletRequest request,
                            AccessTokenAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}

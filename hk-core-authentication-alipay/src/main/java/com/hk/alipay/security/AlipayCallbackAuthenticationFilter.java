package com.hk.alipay.security;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.hk.commons.util.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author huangkai
 */
public class AlipayCallbackAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * @see https://docs.open.alipay.com/289/105656
     */
    private static final String AUTH_CODE_NAME = "auth_code";

    private static final String STATE_NAME = "state";

    private AlipayClient alipayClient;

    private final String state;

    private final String scope;

    public AlipayCallbackAuthenticationFilter(String defaultFilterProcessesUrl,
                                              AlipayClient alipayClient, String state, String scope) {
        super(defaultFilterProcessesUrl);
        setAuthenticationDetailsSource(new AlipayAuthenticationDetailsSource());
        this.alipayClient = alipayClient;
        this.state = state;
        this.scope = scope;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authCode = request.getParameter(AUTH_CODE_NAME);
        if (StringUtils.isEmpty(authCode)) {
            throw new AuthenticationServiceException("authCode不能为空");
        }
        if (StringUtils.isNotEmpty(state)
                && StringUtils.notEquals(state, request.getParameter(STATE_NAME))) {
            throw new AuthenticationServiceException("登录失败，跨站请求伪造攻击");
        }
        AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
        oauthTokenRequest.setGrantType("authorization_code");
        oauthTokenRequest.setCode(authCode);
        try {
//             根据 authCode 获取  accessToken
            AlipaySystemOauthTokenResponse tokenResponse = alipayClient.execute(oauthTokenRequest);
            AlipayAuthenticationToken authenticationToken;
////            根据 accessToken 获取用户其它信息,只有授权在 auth_user 才可以获取用户其它信息，需要用户同意授权
            if (StringUtils.contains(scope, "auth_user")) {
                AlipayUserInfoShareRequest userInfoShareRequest = new AlipayUserInfoShareRequest();
                AlipayUserInfoShareResponse userInfo = alipayClient.execute(userInfoShareRequest, tokenResponse.getAccessToken());
                authenticationToken = new AlipayAuthenticationToken(userInfo);
            } else { // 静默授权  scope = auth_base
                authenticationToken = new AlipayAuthenticationToken(tokenResponse);
            }
            setDetails(request, authenticationToken);
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (AlipayApiException e) {
            throw new AuthenticationServiceException(e.getErrMsg());
        }
    }

    private void setDetails(HttpServletRequest request, AlipayAuthenticationToken authenticationToken) {
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}

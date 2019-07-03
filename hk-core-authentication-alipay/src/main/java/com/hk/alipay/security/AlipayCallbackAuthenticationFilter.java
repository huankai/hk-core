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

    public AlipayCallbackAuthenticationFilter(String defaultFilterProcessesUrl, AlipayClient alipayClient, String state) {
        super(defaultFilterProcessesUrl);
        this.alipayClient = alipayClient;
        this.state = state;
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
            AlipayUserInfoShareRequest userInfoShareRequest = new AlipayUserInfoShareRequest();
////            根据 accessToken 获取用户信息
            AlipayUserInfoShareResponse userInfo = alipayClient.execute(userInfoShareRequest, tokenResponse.getAccessToken());
            AlipayAuthenticationToken authenticationToken = new AlipayAuthenticationToken(userInfo);
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

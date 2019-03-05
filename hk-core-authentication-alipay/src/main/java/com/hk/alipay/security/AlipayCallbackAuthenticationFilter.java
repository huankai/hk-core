package com.hk.alipay.security;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.hk.alipay.security.AlipayAuthenticationToken;
import com.hk.core.authentication.api.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
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
     * @see https://docs.open.alipay.com/218/105326/
     */
    private static final String AUTH_CODE_NAME = "auth_code";

    private AlipayClient alipayClient;

    public AlipayCallbackAuthenticationFilter(String defaultFilterProcessesUrl, AlipayClient alipayClient) {
        super(defaultFilterProcessesUrl);
        this.alipayClient = alipayClient;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authCode = request.getParameter(AUTH_CODE_NAME);
        if (StringUtils.isEmpty(authCode)) {
            throw new AuthenticationServiceException("authCode不能为空");
        }
        AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
        oauthTokenRequest.setGrantType("authorization_code");
        oauthTokenRequest.setCode(authCode);
        try {
//             根据 authCode 获取  accessToken
            AlipaySystemOauthTokenResponse tokenResponse = alipayClient.execute(oauthTokenRequest);
            AlipayUserInfoShareRequest userInfoShareRequest = new AlipayUserInfoShareRequest();
//            根据 accessToken 获取用户信息
            AlipayUserInfoShareResponse userInfo = alipayClient.execute(userInfoShareRequest, tokenResponse.getAccessToken());
//            UserPrincipal principal = new UserPrincipal(userInfo.getUserId(), userInfo.getUserName(), userInfo.getUserName(), userInfo.getNickName(),
//                    null, null, null, userInfo.getGender(),
//                    userInfo.getAvatar(), Integer.parseInt(userInfo.getUserStatus()));
            UserPrincipal principal = null;
            AlipayAuthenticationToken authenticationToken = new AlipayAuthenticationToken(principal);
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

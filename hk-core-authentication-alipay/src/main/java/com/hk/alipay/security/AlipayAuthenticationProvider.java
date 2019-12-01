package com.hk.alipay.security;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.hk.commons.util.AssertUtils;
import com.hk.core.authentication.api.PostAuthenticationHandler;
import com.hk.core.authentication.api.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author huangkai
 * @date 2019/3/5 17:50
 */
@RequiredArgsConstructor
public class AlipayAuthenticationProvider implements AuthenticationProvider {

    private final PostAuthenticationHandler<UserPrincipal, String> authenticationHandler;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AlipayResponse alipayResponse = (AlipayResponse) authentication.getPrincipal();
        UserPrincipal principal;
        if (null != authenticationHandler) {
            if (alipayResponse instanceof AlipayUserInfoShareResponse) {
                principal = authenticationHandler.handler(((AlipayUserInfoShareResponse) alipayResponse).getUserId());
            } else if (alipayResponse instanceof AlipaySystemOauthTokenResponse) {
                principal = authenticationHandler.handler(((AlipaySystemOauthTokenResponse) alipayResponse).getUserId());
            } else {
                throw new AuthenticationServiceException("未知的认证");
            }
        } else {
            principal = new UserPrincipal();
            if (alipayResponse instanceof AlipayUserInfoShareResponse) {
                AlipayUserInfoShareResponse shareResponse = (AlipayUserInfoShareResponse) alipayResponse;
                // TODO 因为这里的用户 id 为 Long 类型，而 阿里返回的是string 类型，不能强制类型转换，这里需要根据具体业务还编写逻辑
//                principal.setUserId(shareResponse.getUserId());
                principal.setAccount(shareResponse.getUserId());
                principal.setRealName(shareResponse.getUserName());
                // 设置其它用户信息
            } else if (alipayResponse instanceof AlipaySystemOauthTokenResponse) {
                // TODO 因为这里的用户 id 为 Long 类型，而 阿里返回的是string 类型，不能强制类型转换，这里需要根据具体业务还编写逻辑
//                principal.setUserId(((AlipaySystemOauthTokenResponse) alipayResponse).getUserId());
            } else {
                throw new AuthenticationServiceException("未知的认证");
            }
        }

        AssertUtils.notNull(principal, "principal Must not be null.");
        return new AlipayAuthenticationToken(principal, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AlipayAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

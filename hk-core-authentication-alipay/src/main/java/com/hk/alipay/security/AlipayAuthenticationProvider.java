package com.hk.alipay.security;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.hk.commons.util.AssertUtils;
import com.hk.core.authentication.api.PostAuthenticationHandler;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.api.enums.ThirdAccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

/**
 * @author huangkai
 * @date 2019/3/5 17:50
 */
@RequiredArgsConstructor
public class AlipayAuthenticationProvider implements AuthenticationProvider {

    private final PostAuthenticationHandler<UserPrincipal, String> authenticationHandler;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var alipayResponse = (AlipayResponse) authentication.getPrincipal();
        UserPrincipal principal = null;
        if (null != authenticationHandler) {
            if (alipayResponse instanceof AlipayUserInfoShareResponse) {
                principal = authenticationHandler.handler(((AlipayUserInfoShareResponse) alipayResponse).getUserId());
            } else if (alipayResponse instanceof AlipaySystemOauthTokenResponse) {
                principal = authenticationHandler.handler(((AlipaySystemOauthTokenResponse) alipayResponse).getUserId());
            }
        } else {
            principal = new UserPrincipal();
            if (alipayResponse instanceof AlipayUserInfoShareResponse) {
                AlipayUserInfoShareResponse shareResponse = (AlipayUserInfoShareResponse) alipayResponse;
                principal.setThirdOpenId(Map.of(ThirdAccountType.ali.name(), shareResponse.getUserId()));
                principal.setAccount(shareResponse.getUserId());
                principal.setRealName(shareResponse.getUserName());
                principal.setIconPath(shareResponse.getAvatar());
                principal.setEmail(shareResponse.getEmail());
                principal.setPhone(shareResponse.getPhone());
                //TODO 设置其它用户信息
            } else if (alipayResponse instanceof AlipaySystemOauthTokenResponse) {
                principal.setThirdOpenId(Map.of(ThirdAccountType.ali.name(), ((AlipaySystemOauthTokenResponse) alipayResponse).getUserId()));
            }
        }
        AssertUtils.notNull(principal, "认证信息不能为空.");
        return new AlipayAuthenticationToken(principal, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AlipayAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

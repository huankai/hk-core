package com.hk.weixin.security;

import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.api.UserPrincipalService;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


/**
 * 微信二维码认证提供者
 *
 * @author kevin
 * @date 2018年2月8日上午11:25:39
 */
@NoArgsConstructor
public class WechatAuthenticationProvider implements AuthenticationProvider {

    @Setter
    private UserPrincipalService userPrincipalService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if (null != userPrincipalService) {
            /*
             * 返回的对象去数据库查询用户信息、权限等
             */
            principal = userPrincipalService.athenticationSuccess(principal);
        }
        return new WechatAuthenticationToken(principal, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

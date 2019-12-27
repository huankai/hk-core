package com.hk.core.authentication.oauth2.provider.token;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.oauth2.exception.IllegalClientIpTokenException;
import com.hk.core.web.Webs;
import lombok.Setter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Map;

/**
 * 限制在认证中心生成的 token中，加入了 {@link #ipKey} 的值，防止用户的token 被其它ip所使用
 *
 * @author kevin
 * @date 2019-12-18 17:03
 */
public class RequestIpJwtTokenStore extends JwtTokenStore {

    @Setter
    private String ipKey = "ip";

    public RequestIpJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
        super(jwtTokenEnhancer);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken oAuth2AccessToken = super.readAccessToken(tokenValue);
        Map<String, Object> map = oAuth2AccessToken.getAdditionalInformation();
        if (map.containsKey(ipKey) &&
                StringUtils.notEquals(String.valueOf(map.get(ipKey)), Webs.getRemoteAddr(Webs.getHttpServletRequest()))) {
            throw new IllegalClientIpTokenException("认证Token 被盗用");
        }
        return oAuth2AccessToken;
    }
}

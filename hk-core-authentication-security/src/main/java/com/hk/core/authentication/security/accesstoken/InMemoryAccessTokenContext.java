package com.hk.core.authentication.security.accesstoken;

import com.hk.commons.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用内存存储 Token
 *
 * @author huangkai
 * @date 2019/3/5 15:49
 */
public class InMemoryAccessTokenContext implements AccessTokenContext {

    private Map<String, TokenUserPrincipal> TOKEN_MAP = new ConcurrentHashMap<>(128);

    @Override
    public Optional<TokenUserPrincipal> getToken(String token) {
        return Optional.ofNullable(TOKEN_MAP.get(token));
    }

    @Override
    public void removeAccessToken(String token) {
        TOKEN_MAP.remove(token);
    }

    @Override
    public void storeToken(TokenUserPrincipal token) {
        getToken(token.getToken()).ifPresent(item -> {
            if (StringUtils.notEquals(item.getAccount(), token.getAccount())) {
                throw new RuntimeException("Token 已存在"); // 一般不会出现这种情况，请确保每次生成的 Token 值不同。
            }
        });
        TOKEN_MAP.put(token.getToken(), token);
    }

    @Override
    public void refreshToken(TokenUserPrincipal token) {
        TokenUserPrincipal tokenUserPrincipal = getToken(token.getToken())
                .orElseThrow(() -> new RuntimeException("不存在的Token"));
        int time = LocalDateTime.now().getSecond() - tokenUserPrincipal.getStart().getSecond();
        tokenUserPrincipal.setExpire(LocalDateTime.now().plusHours(time));
        TOKEN_MAP.put(token.getToken(), tokenUserPrincipal);
    }

}

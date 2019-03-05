package com.hk.core.authentication.security.accesstoken;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangkai
 * @date 2019/3/5 15:49
 */
public class InMemoryAccessTokenContext implements AccessTokenContext {

    private Map<String, Token> map = new ConcurrentHashMap<>(128);

    @Override
    public Token getToken(String token) {
        return map.get(token);
    }

    @Override
    public void removeAccessToken(String token) {
        map.remove(token);
    }

    @Override
    public void storeToken(Token token) {
        map.put(token.getToken(), token);
    }

    @Override
    public void refreshToken(Token token) {
        map.put(token.getToken(), token);
    }
}

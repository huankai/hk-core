package com.hk.core.authentication.security.accesstoken;

import com.hk.core.authentication.api.UserPrincipal;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author huangkai
 * @date 2019/3/6 17:30
 */
public abstract class TokenAuthenticationProvider {

    /**
     * token 过期时间，单位: 秒
     */
    private final long expire;

    protected TokenAuthenticationProvider(long expire) {
        this.expire = expire;
    }

    protected String generateToken() {
        return Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
    }

    protected final TokenUserPrincipal convertToTokenUserPrincipal(UserPrincipal userPrincipal) {
        return new TokenUserPrincipal(generateToken(), LocalDateTime.now().plusSeconds(expire), userPrincipal);
    }
}

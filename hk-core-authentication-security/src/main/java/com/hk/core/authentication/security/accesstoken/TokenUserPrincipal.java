package com.hk.core.authentication.security.accesstoken;

import com.hk.core.authentication.api.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author huangkai
 * @date 2019/3/5 15:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TokenUserPrincipal extends UserPrincipal implements Serializable {

    /**
     * token
     */
    private String token;

    /**
     * token 生效时间
     */
    private LocalDateTime start;

    /**
     * 过期时间
     */
    private LocalDateTime expire;

    public TokenUserPrincipal(String token, LocalDateTime expire, UserPrincipal principal) {
        super(principal.getUserId(), principal.getAccount(), principal.isProtectUser(),
                principal.getRealName(), principal.getUserType(),
                principal.getPhone(), principal.getEmail(),
                principal.getSex(), principal.getIconPath(),
                principal.getRoles(), principal.getPermissions());
        this.token = token;
        this.start = LocalDateTime.now();
        this.expire = expire;
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expire);
    }
}

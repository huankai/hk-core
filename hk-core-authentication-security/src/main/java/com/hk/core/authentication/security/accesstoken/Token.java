package com.hk.core.authentication.security.accesstoken;

import com.hk.core.authentication.api.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author huangkai
 * @date 2019/3/5 15:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token implements Serializable {

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private LocalDateTime expire;

    /**
     * 用户信息
     */
    private UserPrincipal principal;

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expire);
    }
}

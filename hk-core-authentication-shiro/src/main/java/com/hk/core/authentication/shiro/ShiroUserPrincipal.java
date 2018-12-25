package com.hk.core.authentication.shiro;

import com.hk.core.authentication.api.UserPrincipal;
import lombok.Data;

/**
 * @author huangkai
 * @date 2018-12-25 10:58
 */
@Data
public final class ShiroUserPrincipal {

    /**
     * {@link UserPrincipal}
     */
    private UserPrincipal userPrincipal;

    /**
     * 用户密码
     */
    private String password;

    public ShiroUserPrincipal(UserPrincipal userPrincipal, String password) {
        this.userPrincipal = userPrincipal;
        this.password = password;
    }
}

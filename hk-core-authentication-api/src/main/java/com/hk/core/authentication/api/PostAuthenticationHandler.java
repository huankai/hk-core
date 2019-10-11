package com.hk.core.authentication.api;


/**
 * 认证处理器
 *
 * @author huangkai
 * @date 2019-04-21 22:55
 */
public interface PostAuthenticationHandler<T extends UserPrincipal, P> {

    /**
     * 认证处理
     *
     * @param principal principal
     * @return T
     */
    T handler(P principal);

}

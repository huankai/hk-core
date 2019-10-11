package com.hk.core.authentication.oauth2;

/**
 * 退出登陆参数
 *
 * @author huangkai
 * @date 2019-06-15 21:43
 */
public interface LogoutParameter {

    /**
     * 退出登陆参数名
     */
    String LOGOUT_PARAMETER_NAME = "logout_url";

    /**
     * 认证服务器通知客户端应用退出登陆时的参数名,如 认证服务器通知客户端执行退出操作时: http://127.0.0.1:8080/logout?oauth2LogoutParameter=xxx
     */
    String CLIENT_LOGOUT_PARAMETER_NAME = "oauth2LogoutParameter";
}

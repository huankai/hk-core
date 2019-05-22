package com.hk.oauth2.logout;

/**
 * @author kevin
 * @date 2019-5-22 11:51
 */
public enum LogoutRequestStatus {

    /**
     * 退出请求未完成，一般为异步退出请求
     */
    NOT_ATTEMPTED,

    /**
     * 退出请求失败
     */
    FAILURE,

    /**
     * 退出请求成功
     */
    SUCCESS
}

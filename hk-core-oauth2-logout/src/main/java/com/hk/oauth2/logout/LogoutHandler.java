package com.hk.oauth2.logout;


import com.hk.oauth2.http.LogoutHttpMessage;

/**
 * @author huangkai
 * @date 2019-05-06 22:09
 */
public interface LogoutHandler {

    void handle(LogoutHttpMessage message);

    /**
     * Gets name.
     *
     * @return the name
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }

}

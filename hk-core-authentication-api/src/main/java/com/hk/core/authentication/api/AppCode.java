package com.hk.core.authentication.api;

import java.io.Serializable;

/**
 * @author: kevin
 * @date 2018-07-12 16:30
 */
public class AppCode implements Serializable {

    private String appId;

    private String appCode;

    private String appName;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}

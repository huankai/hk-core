package com.hk.core.authentication.api;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: kevin
 * @date: 2018-07-12 16:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
public class ClientAppInfo implements Serializable {

    private String appId;

    private String appCode;

    private String appName;

    private String appIcon;
}

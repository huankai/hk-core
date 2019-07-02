package com.hk.core.authentication.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * app 信息
 *
 * @author kevin
 * @date 2018-07-12 16:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
public class ClientAppInfo implements Serializable {

    /**
     * appid
     */
    private Long appId;

    /**
     * appCode
     */
    private String appCode;

    /**
     * appName
     */
    private String appName;

    /**
     * appIcon
     */
    private String appIcon;
}

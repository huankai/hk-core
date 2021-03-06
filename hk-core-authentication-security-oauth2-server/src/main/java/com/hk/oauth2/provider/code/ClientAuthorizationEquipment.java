package com.hk.oauth2.provider.code;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户访问 单点登陆服务器客户端 信息
 *
 * @author kevin
 * @date 2019-5-14 16:47
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
public class ClientAuthorizationEquipment implements Serializable {

    /**
     * 远程客户端 ip
     * {@link org.springframework.http.HttpHeaders#HOST}
     */
    private String remoteAddr;

    /**
     * 客户端使用设备
     * {@link org.springframework.http.HttpHeaders#USER_AGENT}
     */
    private String userAgent;
}

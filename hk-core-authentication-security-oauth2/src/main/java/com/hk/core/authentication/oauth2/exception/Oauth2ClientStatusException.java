package com.hk.core.authentication.oauth2.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 当用户使用一个禁用的 client_id 认证时抛出此异常
 *
 * @author huangkai
 * @date 2018-12-28 15:19
 */
@SuppressWarnings("serial")
@JsonSerialize(using = Oauth2ExceptionSerializer.Oauth2ClientStatusExceptionJackson2Serializer.class)
public class Oauth2ClientStatusException extends OAuth2Exception {

    public Oauth2ClientStatusException(String msg) {
        super(msg);
    }

    public Oauth2ClientStatusException(String msg, Throwable t) {
        super(msg, t);
    }
}

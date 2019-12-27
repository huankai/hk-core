package com.hk.core.authentication.oauth2.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * A ip 登陆生成的 token, 当使用另一个 ip 来访问时，则抛出此异常
 *
 * @author kevin
 * @date 2019-12-18 18:18
 */
@JsonSerialize(using = Oauth2ExceptionSerializer.IllegalClientIpTokenExceptionJackson2Serializer.class)
public class IllegalClientIpTokenException extends OAuth2Exception {

    public IllegalClientIpTokenException(String message) {
        super(message);
    }
}

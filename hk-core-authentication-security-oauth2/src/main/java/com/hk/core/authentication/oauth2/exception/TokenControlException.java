package com.hk.core.authentication.oauth2.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * token 登陆次数控制异常，当同一个账号登陆多次时，抛出此异常
 *
 * @author kevin
 * @date 2019-12-18 10:29
 */
@JsonSerialize(using = Oauth2ExceptionSerializer.Oauth2TokenControlExceptionJackson2Serializer.class)
public class TokenControlException extends OAuth2Exception {

    public TokenControlException(String msg) {
        super(msg);
    }

    public TokenControlException(String msg, Throwable t) {
        super(msg, t);
    }

}

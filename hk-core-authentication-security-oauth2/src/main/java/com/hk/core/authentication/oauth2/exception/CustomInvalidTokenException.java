package com.hk.core.authentication.oauth2.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;

/**
 * 自定义 失效的token 异常，
 * spring oauth2 定义的 {@link InvalidTokenException} 返回的格式 json 格式不是我想要的，
 * 这里通过 {@link Oauth2ExceptionSerializer.CustomInvalidTokenExceptionJackson2Serializer} 转换一下
 *
 * @author kevin
 * @date 2019-12-18 10:13
 */
@JsonSerialize(using = Oauth2ExceptionSerializer.CustomInvalidTokenExceptionJackson2Serializer.class)
public class CustomInvalidTokenException extends InvalidTokenException {

    public CustomInvalidTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public CustomInvalidTokenException(String msg) {
        super(msg);
    }

}

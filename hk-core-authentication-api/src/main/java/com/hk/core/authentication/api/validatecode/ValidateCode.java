package com.hk.core.authentication.api.validatecode;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 验证码
 *
 * @author kevin
 * @date 2018-07-27 13:40
 */
@SuppressWarnings("serial")
public class ValidateCode implements Serializable {

    /**
     * 验证码
     */
    @Getter
    private final String code;

    /**
     * 过期时间秒,单位:秒
     */
    @Getter
    private final int expireSecond;

    /**
     * 过期时间
     */
    private final LocalDateTime expireTime;

    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireSecond = expireIn;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    /**
     * 验证码是否过期
     *
     * @return true if expire
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}

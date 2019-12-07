package com.hk.core.authentication.api.enums;

import lombok.Getter;

/**
 * @author kevin
 * @date 2019-12-7 17:19
 */
@Getter
public enum ThirdAccountType {

    wx((byte) 1, "微信"),

    ali((byte) 2, "支付宝");

    private byte value;

    private String text;

    ThirdAccountType(byte value, String text) {
        this.value = value;
        this.text = text;
    }
}

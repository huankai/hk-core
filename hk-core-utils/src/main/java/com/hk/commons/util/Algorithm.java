package com.hk.commons.util;

import lombok.Getter;

/**
 * @author huangkai
 * @date 2019-05-14 22:40
 */
@Getter
public enum Algorithm {

    MD5("MD5"),

    SHA_1("SHA-1"),

    SHA_256("SHA-256"),

    SHA_512("SHA-512");

    private String name;

    Algorithm(String name) {
        this.name = name;
    }
}
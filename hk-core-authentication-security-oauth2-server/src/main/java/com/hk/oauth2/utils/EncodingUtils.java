package com.hk.oauth2.utils;

import lombok.SneakyThrows;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author kevin
 * @date 2019-5-18 13:19
 */
public abstract class EncodingUtils {

    public static String urlEncode(final String value) {
        return urlEncode(value, StandardCharsets.UTF_8.name());
    }

    @SneakyThrows
    public static String urlEncode(final String value, final String encoding) {
        return URLEncoder.encode(value, encoding);
    }
}

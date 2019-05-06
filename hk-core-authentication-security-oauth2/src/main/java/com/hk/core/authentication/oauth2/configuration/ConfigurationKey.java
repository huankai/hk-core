package com.hk.core.authentication.oauth2.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kevin
 * @date 2019-5-6 14:58
 */
@AllArgsConstructor
public final class ConfigurationKey<V> {

    @Getter
    private String name;

    @Getter
    private V value;

}

package com.hk.core.authentication.oauth2.configuration;

import com.hk.commons.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kevin
 * @date 2019-5-6 16:59
 */
@Slf4j
public enum ConfigurationStrategyName {

    WEB_XML(WebXmlConfigurationStrategyImpl.class),

    PROPERTY_FILE(PropertiesConfigurationStrategyImpl.class);

    private final Class<? extends ConfigurationStrategy> configurationStrategyClass;

    ConfigurationStrategyName(final Class<? extends ConfigurationStrategy> configurationStrategyClass) {
        this.configurationStrategyClass = configurationStrategyClass;
    }

    @SuppressWarnings("unchecked")
	public static Class<? extends ConfigurationStrategy> resolveToConfigurationStrategy(final String value) {
        if (StringUtils.isEmpty(value)) {
            return PROPERTY_FILE.configurationStrategyClass;
        }
        for (final ConfigurationStrategyName csn : values()) {
            if (csn.name().equalsIgnoreCase(value)) {
                return csn.configurationStrategyClass;
            }
        }
        try {
            final Class<?> clazz = Class.forName(value);
            if (ConfigurationStrategy.class.isAssignableFrom(clazz)) {
                return (Class<? extends ConfigurationStrategy>) clazz;
            }
        } catch (final ClassNotFoundException e) {
            log.error("Unable to locate strategy {} by name or class name.  Using default strategy instead.", value, e);
        }

        return PROPERTY_FILE.configurationStrategyClass;
    }
}

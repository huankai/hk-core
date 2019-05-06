package com.hk.core.authentication.oauth2.configuration;

import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.BooleanUtils;
import com.hk.commons.util.ClassUtils;
import com.hk.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kevin
 * @date 2019-5-6 16:23
 */
public abstract class BaseConfigurationStrategy implements ConfigurationStrategy {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取 Boolean 类型值
     *
     * @param configurationKey the configuration key.  MUST NOT BE NULL.
     */
    public final boolean getBoolean(final ConfigurationKey<Boolean> configurationKey) {
        return getValue(configurationKey, BooleanUtils::toBoolean);
    }

    /**
     * 获取 Long 类型值
     *
     * @param configurationKey the configuration key.  MUST NOT BE NULL.
     * @return
     */
    public final long getLong(final ConfigurationKey<Long> configurationKey) {
        return getValue(configurationKey, Long::parseLong);
    }

    /**
     * 获取 int 类型值
     * @param configurationKey the configuration key.  MUST NOT BE NULL.
     * @return
     */
    public final int getInt(final ConfigurationKey<Integer> configurationKey) {
        return getValue(configurationKey, Integer::parseInt);
    }

    /**
     * 获取 string 类型值
     * @param configurationKey the configuration key.  MUST NOT BE NULL.
     * @return
     */
    public final String getString(final ConfigurationKey<String> configurationKey) {
        return getValue(configurationKey, value -> value);
    }

    /**
     * @param configurationKey the configuration key.  MUST NOT BE NULL.
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> getClass(final ConfigurationKey<Class<? extends T>> configurationKey) {
        return getValue(configurationKey, value -> {
            try {
                return (Class<? extends T>) ClassUtils.forName(value, null);
            } catch (ClassNotFoundException e) {
                return configurationKey.getValue();
            }
        });
    }

    private <T> T getValue(final ConfigurationKey<T> configurationKey, final Parser<T> parser) {
        final String value = getWithCheck(configurationKey);
        if (StringUtils.isEmpty(value)) {
            logger.trace("No value found for property {}, returning default {}", configurationKey.getName(),
                    configurationKey.getValue());
            return configurationKey.getValue();
        } else {
            logger.trace("Loaded property {} with value {}", configurationKey.getName(), configurationKey.getValue());
        }
        return parser.parse(value);
    }

    private String getWithCheck(final ConfigurationKey<?> configurationKey) {
        AssertUtils.notNull(configurationKey, "configurationKey cannot be null");
        return get(configurationKey);
    }

    /**
     * @param configurationKey
     * @return
     */
    protected abstract String get(ConfigurationKey<?> configurationKey);

    @FunctionalInterface
    private interface Parser<T> {

        T parse(String value);
    }
}

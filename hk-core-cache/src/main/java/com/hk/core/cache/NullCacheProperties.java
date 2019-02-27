package com.hk.core.cache;

import lombok.Data;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author huangkai
 * @date 2019-02-27 14:51
 */
@Data
@ConfigurationProperties(prefix = "spring.cache.redis")
public class NullCacheProperties {

    /**
     * 空值保存时间，防止出现缓存穿透
     * <p>
     * 同时必须开启空值缓存
     *
     * @see CacheProperties.Redis#isCacheNullValues()
     */
    private Duration nullValueTtl = Duration.ofSeconds(20);
}

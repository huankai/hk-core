package com.hk.commons.util;


import com.hk.commons.util.date.DatePattern;
import com.hk.commons.util.date.DateTimeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author kevin
 * @date 2019-10-30 11:23
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SerialUniqueIdGenerator implements IDGenerator<String> {

    private static final SerialUniqueIdGenerator idGenerator = new SerialUniqueIdGenerator();

    private static final Lazy<SnowflakeIdGenerator> snowflakeIdGeneratorLazy = Lazy.of(() -> {
        SnowflakeProperties properties = SpringContextHolder.getBeanIfExist(SnowflakeProperties.class).orElse(new SnowflakeProperties());
        return new SnowflakeIdGenerator(properties.getWorkerId(), properties.getDataCenterId());
    });

    public static SerialUniqueIdGenerator getInstance() {
        return idGenerator;
    }

    @Override
    public String generate() {
        return String.format("%s%s", DateTimeUtils.localDateTimeToString(LocalDateTime.now(), DatePattern.YYYYMMDDHHMMSS),
                snowflakeIdGeneratorLazy.get().generate());
    }
}

package com.hk.commons.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ID生成器
 *
 * @param <T>
 * @author kevin
 * @date 2017年8月22日下午1:32:03
 */
@FunctionalInterface
public interface IDGenerator<T> {

    /**
     * 生成Id
     *
     * @return ID
     */
    T generate();

    /**
     * UUID生成 ，去掉 "-"
     */
    IDGenerator<String> STRING_UUID = () -> UUID.randomUUID().toString()
            .replaceAll(StringUtils.RUNG, StringUtils.EMPTY);

    /**
     * 32 位 使用ThreadLocalRandom获取UUID获取更优的效果 去掉"-"
     */
    IDGenerator<String> UUID_32 = () -> {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong()).toString().replaceAll(StringUtils.RUNG, StringUtils.EMPTY);
    };

    /**
     * MostSignificantBit
     */
    IDGenerator<Long> MOSTSIGN_UUID = () -> UUID.randomUUID().getMostSignificantBits();

}

package com.hk.core.redis;

import com.hk.commons.util.IDGenerator;
import com.hk.commons.util.SerialUniqueIdGenerator;
import com.hk.core.redis.locks.RedisLock;

/**
 * @author kevin
 * @date 2019-12-6 12:27
 */
public class RedisSerialUniqueIdGenerator implements IDGenerator<String> {

    private final IDGenerator<String> idGenerator = SerialUniqueIdGenerator.getInstance();

    private static final String LOCK_KEY = "RedisSerialUniqueIdGenerator.LOCK";

    @Override
    public String generate() {
        var lock = new RedisLock(LOCK_KEY);
        try {
            lock.lock();
            return idGenerator.generate();
        } finally {
            lock.unlock();
        }
    }
}

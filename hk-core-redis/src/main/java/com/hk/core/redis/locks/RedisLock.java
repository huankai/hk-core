package com.hk.core.redis.locks;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.IDGenerator;
import com.hk.commons.util.ObjectUtils;
import com.hk.commons.util.SpringContextHolder;
import org.apache.commons.io.IOUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 基于 Redis setNx 与 lua 脚本实现分布式锁
 *
 * @author huangkai
 * @date 2019/2/20 22:08
 */
public class RedisLock implements Lock {

    /**
     * 默认过期时间: 2 秒
     */
    private static final long EXPIRE_SECONDS = 2;

    /**
     * redis Key
     */
    private final String key;

    /**
     * key 过期时间，防止死锁
     */
    private final long expire;

    private final StringRedisTemplate redisTemplate;

    /**
     * lua 脚本内容，lua 脚本能保证原子性执行
     */
    private static final String LUA_SCRIPT;

    private ThreadLocal<String> LOCAL_VALUE = new ThreadLocal<>();

    static {
        URL resource = RedisLock.class.getClassLoader().getResource("unlock.lua");
        try {
            LUA_SCRIPT = IOUtils.toString(resource, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("lua file not found..");
        }
    }

    public RedisLock(String key) {
        this(key, EXPIRE_SECONDS);
    }

    public RedisLock(String key, long expire) {
        this.key = key;
        this.expire = expire <= 0 ? EXPIRE_SECONDS : expire;
        redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
    }

    /**
     * 获取锁，使用递归，无法使用优雅方式，即当一个线程释放锁后不能自动通知其它等待的客户端来获取锁，而使用递归每 10 ms 来获取一次.
     */
    @Override
    public void lock() {
        if (!tryLock()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // ignore
            }
            lock();
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        if (Thread.interrupted()) {//如果当前线程已中段
            throw new InterruptedException();
        }
        if (!tryLock()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // ignore
            }
            lockInterruptibly();
        }
    }

    /**
     * 尝试获取锁，立即返回。如果返回  true ,加锁成功
     *
     * @return true or false
     */
    @Override
    public boolean tryLock() {
        String value = IDGenerator.STRING_UUID.generate();
        LOCAL_VALUE.set(value);
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, expire, TimeUnit.SECONDS);
        return ObjectUtils.defaultIfNull(result, Boolean.FALSE);
    }

    /**
     * 在指定的时间段获取锁，超出指定的时间立即返回
     *
     * @param time time
     * @param unit unit
     * @return true or false
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        long max = System.nanoTime() + unit.toNanos(time);
        while (System.nanoTime() < max) {
            boolean lock = tryLock();
            if (lock) return true;
        }
        return false;
    }

    /**
     * 解锁
     */
    @Override
    public void unlock() {
        redisTemplate.execute(new DefaultRedisScript<>(LUA_SCRIPT, Long.class),
                ArrayUtils.asArrayList(key), LOCAL_VALUE.get());
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("UnsupportedOperation");
    }
}

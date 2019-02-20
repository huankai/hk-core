package com.hk.core.redis.locks;

import com.hk.commons.util.SpringContextHolder;
import org.apache.commons.io.IOUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.types.Expiration;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
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
     * redis Key
     */
    private final String key;

    /**
     * 连接工厂
     */
    private RedisConnectionFactory connectionFactory;

    /**
     * lua 脚本内容，lua 脚本能保证原子性执行
     */
    private static final String LUA_SCRIPT;

    private ThreadLocal<String> LOCAL_VALUE = new ThreadLocal<>();

    static {
        URL resource = RedisLock.class.getClassLoader().getResource("unlock.lua");
        String content;
        try {
            content = IOUtils.toString(resource, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("lua file not found..");
        }
        LUA_SCRIPT = content;
    }

    public RedisLock(String key) {
        this.key = key;
        connectionFactory = SpringContextHolder.getBean(RedisConnectionFactory.class);
    }

    /**
     * 获取锁，使用递归，无法使用优雅方式，即当一个线程释放锁后不能自动通知其它等待的客户端来获取锁，而使用递归每 10 ms 来获取一次.
     */
    @Override
    public void lock() {
        if (tryLock()) {
            return;
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // ignore
        }
        lock();
    }

    @Override
    public void lockInterruptibly() {
        throw new UnsupportedOperationException("UnsupportedOperation");
    }

    /**
     * 如果返回  true ,加锁成功
     *
     * @return true or false
     */
    @Override
    public boolean tryLock() {
        String value = UUID.randomUUID().toString();
        RedisConnection connection = connectionFactory.getConnection();
        try {
            return connection.set(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8),
                    Expiration.seconds(2), RedisStringCommands.SetOption.ifAbsent());
        } finally {
            connection.close();
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        return false;
    }

    /**
     * 解锁
     */
    @Override
    public void unlock() {
        RedisConnection connection = connectionFactory.getConnection();
        try {
            //执行 LUA 脚本
            connection.eval(LUA_SCRIPT.getBytes(StandardCharsets.UTF_8),
                    ReturnType.BOOLEAN, 2,
                    key.getBytes(StandardCharsets.UTF_8), LOCAL_VALUE.get().getBytes(StandardCharsets.UTF_8));
        } finally {
            connection.close();
        }
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("UnsupportedOperation");
    }
}

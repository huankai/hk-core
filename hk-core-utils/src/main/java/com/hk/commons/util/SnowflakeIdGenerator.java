package com.hk.commons.util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 雪花id算法
 *
 * @author kevin
 * @date 2018-10-26 15:48
 */
public class SnowflakeIdGenerator implements IDGenerator<Long> {

    /**
     * 服务器第一次上线时间点, 设置后不允许修改 ,开始时间截 (2015-01-01)
     */
    private static final long START_STMP = 1489111610226L;

    /**
     * 机器id所占的位数
     */
    private static final long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private static final long dataCenterIdBits = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private static final long maxWorkerId = ~(-1L << workerIdBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private static final long maxDataCenterId = ~(-1L << dataCenterIdBits);

    /**
     * 序列在id中占的位数
     */
    private static final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private static final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private static final long dataCenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private static final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private static final long sequenceMask = ~(-1L << sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private int workerId;

    /**
     * 数据中心ID(0~31)
     */
    private int dataCenterId;

    /**
     * 并发控制
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator() {
        this.dataCenterId = getDataCenterId((int) maxDataCenterId);
        this.workerId = getMaxWorkerId(dataCenterId, (int) maxWorkerId);
    }

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param dataCenterId 数据中心ID (0~31)
     */
    public SnowflakeIdGenerator(int workerId, int dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    protected static int getMaxWorkerId(int dataCenterId, int maxWorkerId) {
        var mpid = new StringBuilder();
        mpid.append(dataCenterId);
        var name = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isNotEmpty(name)) {
            /*
             * GET jvmPid
             */
            mpid.append(name.split("@")[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    protected static int getDataCenterId(int dataCenterId) {
        int id = 0;
        try {
            var ip = InetAddress.getLocalHost();
            var network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1;
            } else {
                var mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & mac[mac.length - 1]) | (0x0000FF00 & ((mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (dataCenterId + 1);
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return id;
    }

    // ==============================Methods==========================================

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        var timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    private long timeGen() {
        return SystemClock.now();
    }

    /**
     * 获得下一个ID (线程安全)
     *
     * @return SnowflakeId
     */
    @Override
    public synchronized Long generate() {
        var timestamp = timeGen();
        //闰秒
        if (timestamp < lastTimestamp) {
            var offset = lastTimestamp - timestamp;
            if (offset <= 5) {
                try {
                    wait(offset << 1);
                    timestamp = timeGen();
                    if (timestamp < lastTimestamp) {
                        throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
            }
        }
        if (lastTimestamp == timestamp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                // 同一毫秒的序列数已经达到最大
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒内，序列号置为 1 - 3 随机数
            sequence = ThreadLocalRandom.current().nextLong(1, 3);
        }
        lastTimestamp = timestamp;
        // 时间戳部分 | 数据中心部分 | 机器标识部分 | 序列号部分
        return ((timestamp - START_STMP) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

}

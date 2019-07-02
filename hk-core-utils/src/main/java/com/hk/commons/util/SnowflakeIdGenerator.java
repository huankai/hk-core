package com.hk.commons.util;

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
    private static final long twepoch = 1489111610226L;

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
     * 最大容忍时间, 单位毫秒, 即如果时钟只是回拨了该变量指定的时间, 那么等待相应的时间即可;
     * 考虑到sequence服务的高性能, 这个值不易过大
     */
    private static final long MAX_BACKWARD_MS = 3;

    /**
     * 工作机器ID(0~31)
     */
    private int workerId;

    /**
     * 数据中心ID(0~31)
     */
    private int dataCenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    //==============================Constructors=====================================

    public SnowflakeIdGenerator() {
        this(1, 1);
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

    // ==============================Methods==========================================

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
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
        return System.currentTimeMillis();
    }


    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    @Override
    public synchronized Long generate() {
        long timestamp = timeGen();
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退
        if (timestamp < lastTimestamp) {
            // 如果时钟回退在最大容忍时间内，线程等待
            if (lastTimestamp - timestamp < MAX_BACKWARD_MS) {
                try {
                    Thread.sleep(lastTimestamp - timestamp);
                } catch (InterruptedException e) {
                    // ignore
                }
            } else {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
            }
        }
        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }
        //上次生成ID的时间截
        lastTimestamp = timestamp;
        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (dataCenterId << dataCenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

}

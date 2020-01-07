package com.hk.commons.util;


import com.hk.commons.util.date.DatePattern;
import com.hk.commons.util.date.DateTimeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.LongAdder;

/**
 * 唯一 id生成器，根据时间　　yyyyMMddHHmmss + 0000 +　生成的自增数字
 * 生成的位数为  yyyyMMddHHmmss 所占用的位数( 14) + {@link #digit}，当使用 {@link #longAdder} 生成的位数不足时，使用 0　补齐
 * 每天都会从 0 重新记数
 *
 * @author kevin
 * @date 2019-10-30 11:23
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SerialUniqueIdGenerator implements IDGenerator<String> {

    /**
     * 生成的位数
     */
    private static final int digit = 14;

    /**
     * 补位字符串
     */
    private static final String PATCH_POSITION = "0";

    private LongAdder longAdder = new LongAdder();

    private volatile LocalDateTime todayMaxTime;

    private static final SerialUniqueIdGenerator INSTANCE = new SerialUniqueIdGenerator();

    public static SerialUniqueIdGenerator getInstance() {
        return INSTANCE;
    }

    @Override
    public String generate() {
        var now = LocalDateTime.now();
        if (null == todayMaxTime) {
            todayMaxTime = DateTimeUtils.getLocalDateTimeEnd(now);
        }
        if (now.isAfter(todayMaxTime)) {
            todayMaxTime = DateTimeUtils.getLocalDateTimeEnd(now);
            longAdder.reset(); //新的一天，从 0 开始记数
        }
        longAdder.increment();
        var value = longAdder.longValue();
        var length = String.valueOf(value).length();
        var repeat = length > digit ? String.valueOf(value) : PATCH_POSITION.repeat(digit - length);
        return String.format("%s%s%s", DateTimeUtils.localDateTimeToString(now, DatePattern.YYYYMMDDHHMMSS),
                repeat, value);
    }

    /*　*********************** 测试，模拟 1000 个线程、循环测试生成的 id是否重复　*****************　*/
//    public static void main(String[] args) {
//        var idGenerator = SerialUniqueIdGenerator.getInstance();
//        var num = 1000;
//        var cyclicBarrier = new CyclicBarrier(num);
//        for (int i = 0; i < num; i++) {
//            new SimpleCyclicBarrier(cyclicBarrier, idGenerator).start();
//        }
//    }
//
//    private static class SimpleCyclicBarrier extends Thread {
//
//        private CyclicBarrier cyclicBarrier;
//
//        private SerialUniqueIdGenerator idGenerator;
//
//        private SimpleCyclicBarrier(CyclicBarrier cyclicBarrier, SerialUniqueIdGenerator idGenerator) {
//            this.cyclicBarrier = cyclicBarrier;
//            this.idGenerator = idGenerator;
//        }
//
//        @Override
//        public void run() {
//            var threadName = Thread.currentThread().getName();
//            System.out.println(threadName + "start...");
//            try {
//                cyclicBarrier.await();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            for (int i = 0; i < 10000; i++) {
//                System.out.println(threadName + ",i:" + i + ",value:" + idGenerator.generate());
//            }
//        }
//    }
}

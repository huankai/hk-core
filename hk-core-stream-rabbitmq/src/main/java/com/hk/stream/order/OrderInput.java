package com.hk.stream.order;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author huangkai
 * @date 2019-04-14 20:47
 */
public interface OrderInput {

    String GENERATE_ORDER = "generate_order_input";

    /**
     * 生成订单
     */
    @Input(GENERATE_ORDER)
    SubscribableChannel generateOrder();

}

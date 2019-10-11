package com.hk.stream.order;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author huangkai
 * @date 2019-04-14 20:47
 */
public interface OrderOutput {

    String GENERATE_ORDER = "generate_order_output";

    /**
     * 生成订单
     */
    @Output(GENERATE_ORDER)
    MessageChannel generateOrder();

}

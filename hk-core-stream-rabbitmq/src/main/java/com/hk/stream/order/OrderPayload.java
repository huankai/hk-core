package com.hk.stream.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author huangkai
 * @date 2019-04-14 20:47
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderPayload implements Serializable {

    private String id;

    private String orderCode;

    private String body;
}

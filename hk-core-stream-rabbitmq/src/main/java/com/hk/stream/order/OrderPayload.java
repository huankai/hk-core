package com.hk.stream.order;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author huangkai
 * @date 2019-04-14 20:47
 */
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("serial")
public class OrderPayload implements Serializable {

    private String id;

    private String orderCode;

    private String body;
}

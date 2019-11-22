package com.hk.commons.sms;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 手机号发送结果
 *
 * @author kevin
 * @date 2019-11-22 10:19
 */
@Data
@Accessors(chain = true)
public class SmsSenderResult implements Serializable {

    /**
     * 发送成功的手机号
     */
    private Collection<String> successPhones;

    /**
     * 发送失败的手机号
     */
    private Collection<String> failPhones;
}

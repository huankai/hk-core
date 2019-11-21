package com.hk.commons.sms;

import java.util.Set;

/**
 * 手机号发送前验证过滤器
 *
 * @author kevin
 * @date 2019-11-21 14:37
 */
public interface SmsSenderFilter {

    /**
     * 过滤：
     * 如：一个手机号，每分钟只能发送一次、防止用户疯狂的发送轰炸短信
     * 如：一个客户端，每分钟只能发送一次
     *
     * @param phones 要发送的手机号
     * @return 可以发送的手机号
     */
    Set<String> filter(String... phones);

}

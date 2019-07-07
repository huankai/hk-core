package com.hk.commons.sms;

import com.hk.commons.asyn.Asyn;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kevin
 * @date 2018-07-27 08:53
 */
@Slf4j
public class LoggerSmsCodeSender implements SmsCodeSender {

    @Override
    public void send(String mobile, String code, int expireSecond) {
        Asyn asyn = () -> log.info("[XXX] {},登陆验证码: {},请在 {} 分钟内完成验证.", mobile, code, expireSecond / 60);
        asyn.start();
    }
}

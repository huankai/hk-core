package com.hk.commons.sms;

import com.hk.commons.JsonResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author kevin
 * @date 2019-8-10 14:44
 */
@Slf4j
public class LoggerSmsSender extends AbstractSmsSender {

    @Override
    protected JsonResult<SmsSenderResult> doSendSms(Set<String> phones, String message) {
        log.info("Send Phone:{} ,Message: {}", phones, message);
        return new JsonResult<>(new SmsSenderResult().setSuccessPhones(phones));
    }
}

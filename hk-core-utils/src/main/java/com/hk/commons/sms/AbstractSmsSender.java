package com.hk.commons.sms;

import com.hk.commons.JsonResult;
import com.hk.commons.util.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @author kevin
 * @date 2019-8-10 14:37
 */
public abstract class AbstractSmsSender<R> implements SmsSender<R> {

    private static Lazy<TemplateMessageService> templateMessageService = Lazy.of(() -> SpringContextHolder.getBean(TemplateMessageService.class));

    @Override
    public JsonResult<R> sendSms(Collection<String> phones, String message) throws IOException {
        validatePhone(phones);
        AssertUtils.notEmpty(message, "发送消息不能为空");
        return doSendSms(phones, message);
    }

    @Override
    public JsonResult<R> sendTemplateSms(Collection<String> phones, String smsTemplateId, Map<String, ?> templateParameter) throws IOException {
        String message = templateMessageService.get().getTemplateContent(smsTemplateId);
        return sendSms(phones, StringUtils.processTemplate(message, templateParameter));
    }

    protected abstract JsonResult<R> doSendSms(Collection<String> phones, String message) throws IOException;

    private void validatePhone(Collection<String> phones) {
        AssertUtils.notNull(phones, "验证手机号不能为空");
        for (String phone : phones) {
            AssertUtils.isTrue(ValidateUtils.isMobilePhone(phone), "手机号格式不正确:" + phone);
        }
    }
}
